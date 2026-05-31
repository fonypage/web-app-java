package com.app.java.WebAppJava.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;

@Service
public class DockerJudgeService {

    public static class RunResult {
        public String status;
        public String stdout;
        public String stderr;
        public int durationMs;
        public int passed;
        public int total;

        public RunResult(String status, String stdout, String stderr, int durationMs, int passed, int total) {
            this.status = status;
            this.stdout = stdout;
            this.stderr = stderr;
            this.durationMs = durationMs;
            this.passed = passed;
            this.total = total;
        }
    }

    private static final String IMAGE = "eclipse-temurin:17-jdk";
    private static final int OUT_LIMIT = 64_000;

    private static final String MAIN_JAVA = """
        import java.io.*;
        import java.nio.charset.StandardCharsets;

        public class Main {
            public static void main(String[] args) throws Exception {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream in = System.in;
                byte[] buf = new byte[8192];
                int r;
                while ((r = in.read(buf)) != -1) {
                    baos.write(buf, 0, r);
                }
                String input = baos.toString(StandardCharsets.UTF_8);
                String out = Solution.solve(input);
                if (out == null) out = "";
                System.out.print(out);
            }
        }
        """;

    public RunResult judgeJUnit(String solutionJava, String junitTestJava, int timeLimitMs, int memoryMb) {
        long start = System.nanoTime();
        Path dir = null;

        try {
            dir = Files.createTempDirectory("judge-junit-");

            Files.writeString(dir.resolve("Solution.java"), solutionJava, StandardCharsets.UTF_8);
            Files.writeString(dir.resolve("TaskTest.java"), junitTestJava, StandardCharsets.UTF_8);

            copyResourceToFile("/judge/junit-platform-console-standalone.jar", dir.resolve("junit.jar"));

            int compileLimitMs = Math.max(60000, timeLimitMs * 10);

            int oneShotLimitMs = Math.max(60000, timeLimitMs * 10);

            ExecResult run = dockerExec(
                    dir,
                    List.of("bash", "-lc",
                            "javac -encoding UTF-8 -cp junit.jar Solution.java TaskTest.java 2> compile.err || true; " +
                                    "if [ -s compile.err ]; then " +
                                    "  echo \"__CE__\"; cat compile.err; exit 2; " +
                                    "fi; " +
                                    "java -jar junit.jar -cp . --select-class TaskTest --details=summary --disable-ansi-colors"
                    ),
                    oneShotLimitMs,
                    memoryMb,
                    null
            );

            if (run.timedOut) {
                return new RunResult("TLE", limit(run.stdout, OUT_LIMIT), "Time limit exceeded", msSince(start), 0, 0);
            }

            if (run.exitCode == 2) {
                String report = (run.stdout == null ? "" : run.stdout);
                report = report.replaceFirst("__CE__\\R?", "");
                if (report.isBlank()) report = "Compilation failed (no stderr captured)";
                return new RunResult("CE", "", limit(report, OUT_LIMIT), msSince(start), 0, 0);
            }

            String report = (run.stdout == null ? "" : run.stdout) + "\n" + (run.stderr == null ? "" : run.stderr);

            int total = extractAny(report,
                    "(?i)tests found\\s*:\\s*(\\d+)",
                    "(?i)\\[\\s*(\\d+)\\s+tests\\s+found\\s*\\]",
                    "(?i)(\\d+)\\s+tests\\s+found"
            );

            int successful = extractAny(report,
                    "(?i)tests successful\\s*:\\s*(\\d+)",
                    "(?i)\\[\\s*(\\d+)\\s+tests\\s+successful\\s*\\]",
                    "(?i)(\\d+)\\s+tests\\s+successful"
            );

            int failed = extractAny(report,
                    "(?i)tests failed\\s*:\\s*(\\d+)",
                    "(?i)\\[\\s*(\\d+)\\s+tests\\s+failed\\s*\\]",
                    "(?i)(\\d+)\\s+tests\\s+failed"
            );

            int aborted = extractAny(report,
                    "(?i)tests aborted\\s*:\\s*(\\d+)",
                    "(?i)\\[\\s*(\\d+)\\s+tests\\s+aborted\\s*\\]",
                    "(?i)(\\d+)\\s+tests\\s+aborted"
            );

            int skipped = extractAny(report,
                    "(?i)tests skipped\\s*:\\s*(\\d+)",
                    "(?i)\\[\\s*(\\d+)\\s+tests\\s+skipped\\s*\\]",
                    "(?i)(\\d+)\\s+tests\\s+skipped"
            );
            if (total == 0) {
                total = successful + failed + aborted + skipped;
            }
            int passed = Math.max(0, successful);

            if (run.exitCode == 0) {
                return new RunResult("OK", "", "All tests passed", msSince(start), passed, total);
            } else {
                if (report.isBlank()) report = "Tests failed (no report captured)";
                return new RunResult("WA", "", limit(report, OUT_LIMIT), msSince(start), passed, total);
            }

        } catch (Exception e) {
            return new RunResult("RE", "", "Runner error: " + e.getMessage(), msSince(start), 0, 0);
        } finally {
            if (dir != null) {
                try { deleteRecursively(dir); } catch (Exception ignored) {}
            }
        }
    }

    private void copyResourceToFile(String resourcePath, Path target) throws IOException {
        try (InputStream in = DockerJudgeService.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new FileNotFoundException("Resource not found: " + resourcePath);
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public RunResult judge(String solutionJava, List<TestCase> cases, int timeLimitMs, int memoryMb) {
        long start = System.nanoTime();
        Path dir = null;

        try {
            dir = Files.createTempDirectory("judge-1c-");
            Files.writeString(dir.resolve("Solution.java"), solutionJava, StandardCharsets.UTF_8);
            Files.writeString(dir.resolve("Main.java"), MAIN_JAVA, StandardCharsets.UTF_8);

            Path casesDir = Files.createDirectories(dir.resolve("cases"));
            for (int i = 0; i < cases.size(); i++) {
                Files.writeString(casesDir.resolve("in_" + (i + 1) + ".txt"), cases.get(i).input, StandardCharsets.UTF_8);
                Files.writeString(casesDir.resolve("exp_" + (i + 1) + ".txt"), cases.get(i).expected, StandardCharsets.UTF_8);
            }

            String script = """
            set -e

            javac -encoding UTF-8 -Xlint:none Main.java Solution.java 2> compile.err || true
            if [ -s compile.err ]; then
              echo "STATUS=CE"
              echo "PASSED=0"
              echo "TOTAL=%d"
              echo "LOG_BEGIN"
              cat compile.err
              echo "LOG_END"
              exit 0
            fi

            pass=0
            total=%d
            i=1
            while [ $i -le $total ]; do
              out=$(java Main < cases/in_${i}.txt 2> run.err || true)
              if [ -s run.err ]; then
                echo "STATUS=RE"
                echo "PASSED=$pass"
                echo "TOTAL=$total"
                echo "LOG_BEGIN"
                cat run.err
                echo "LOG_END"
                exit 0
              fi

              out_norm=$(printf "%%s" "$out" | tr -d '\\r' | sed -e 's/[[:space:]]*$//')
              exp=$(cat cases/exp_${i}.txt)
              exp_norm=$(printf "%%s" "$exp" | tr -d '\\r' | sed -e 's/[[:space:]]*$//')

              if [ "$out_norm" = "$exp_norm" ]; then
                pass=$((pass+1))
              else
                echo "STATUS=WA"
                echo "PASSED=$pass"
                echo "TOTAL=$total"
                echo "LOG_BEGIN"
                echo "Wrong answer on test #$i"
                echo
                echo "EXPECTED:"
                cat cases/exp_${i}.txt
                echo
                echo "ACTUAL:"
                printf "%%s" "$out"
                echo
                echo "LOG_END"
                exit 0
              fi

              i=$((i+1))
            done

            echo "STATUS=OK"
            echo "PASSED=$pass"
            echo "TOTAL=$total"
            echo "LOG_BEGIN"
            echo "All tests passed"
            echo "LOG_END"
            """.formatted(cases.size(), cases.size());

            Files.writeString(dir.resolve("run.sh"), script, StandardCharsets.UTF_8);

            ExecResult run = dockerExec(
                    dir,
                    List.of("bash", "-lc", "chmod +x run.sh && ./run.sh"),
                    Math.max(20000, timeLimitMs * Math.max(1, cases.size()) * 2),
                    memoryMb,
                    null
            );

            if (run.timedOut) {
                return new RunResult("TLE", "", "Time limit exceeded", msSince(start), 0, cases.size());
            }
            if (run.exitCode != 0) {
                String err = (run.stderr == null || run.stderr.isBlank()) ? "Runner error" : run.stderr;
                return new RunResult("RE", "", limit(err, OUT_LIMIT), msSince(start), 0, cases.size());
            }

            ParsedStdout parsed = parseStdout(run.stdout);
            String status = parsed.status == null ? "RE" : parsed.status;
            int passed = parsed.passed;
            int total = parsed.total == 0 ? cases.size() : parsed.total;

            return new RunResult(status, "", limit(parsed.log, OUT_LIMIT), msSince(start), passed, total);

        } catch (Exception e) {
            return new RunResult("RE", "", "Runner error: " + e.getMessage(), msSince(start), 0, cases.size());
        } finally {
            if (dir != null) {
                try { deleteRecursively(dir); } catch (Exception ignored) {}
            }
        }
    }

    private static class ParsedStdout {
        String status;
        int passed;
        int total;
        String log;
    }

    private static ParsedStdout parseStdout(String stdout) {
        ParsedStdout p = new ParsedStdout();
        if (stdout == null) {
            p.log = "";
            return p;
        }

        StringBuilder log = new StringBuilder();
        boolean inLog = false;

        for (String line : stdout.split("\\R")) {
            if ("LOG_BEGIN".equals(line)) { inLog = true; continue; }
            if ("LOG_END".equals(line)) { inLog = false; continue; }

            if (inLog) {
                log.append(line).append('\n');
                continue;
            }

            if (line.startsWith("STATUS=")) p.status = line.substring("STATUS=".length()).trim();
            else if (line.startsWith("PASSED=")) p.passed = parseIntSafe(line.substring("PASSED=".length()).trim());
            else if (line.startsWith("TOTAL=")) p.total = parseIntSafe(line.substring("TOTAL=".length()).trim());
        }

        p.log = log.toString().trim();
        return p;
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    public static class TestCase {
        public final String input;
        public final String expected;
        public final boolean hidden;
        public TestCase(String input, String expected, boolean hidden) {
            this.input = input == null ? "" : input;
            this.expected = expected == null ? "" : expected;
            this.hidden = hidden;
        }
    }

    private record ExecResult(int exitCode, String stdout, String stderr, boolean timedOut) {}

    private ExecResult dockerExec(Path workdir, List<String> cmd, int timeLimitMs, int memoryMb, String stdin) throws IOException, InterruptedException {
        List<String> base = new ArrayList<>(List.of(
                "docker", "run", "--rm",
                "--pull", "never",
                "--network", "none",
                "--cpus", "0.5",
                "--memory", memoryMb + "m",
                "--pids-limit", "128",
                "-v", workdir.toAbsolutePath().toString() + ":/work",
                "-w", "/work",
                IMAGE
        ));
        base.addAll(cmd);

        ProcessBuilder pb = new ProcessBuilder(base);
        Process p = pb.start();

        if (stdin != null) {
            try (OutputStream os = p.getOutputStream()) {
                os.write(stdin.getBytes(StandardCharsets.UTF_8));
            }
        } else {
            p.getOutputStream().close();
        }

        boolean ok = p.waitFor(timeLimitMs, java.util.concurrent.TimeUnit.MILLISECONDS);
        if (!ok) {
            p.destroyForcibly();
            return new ExecResult(-1, "", "", true);
        }

        String out = readLimited(p.getInputStream(), OUT_LIMIT);
        String err = readLimited(p.getErrorStream(), OUT_LIMIT);
        return new ExecResult(p.exitValue(), out, err, false);
    }

    private static String normalize(String s) {
        if (s == null) return "";
        s = s.replace("\r\n", "\n");
        return s.trim();
    }

    private static int msSince(long startNano) {
        return (int) Duration.ofNanos(System.nanoTime() - startNano).toMillis();
    }

    private static String readLimited(InputStream is, int limit) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int read;
        int total = 0;
        while ((read = is.read(buf)) != -1) {
            int can = Math.min(read, limit - total);
            if (can > 0) baos.write(buf, 0, can);
            total += can;
            if (total >= limit) break;
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    private static String limit(String s, int limit) {
        if (s == null) return "";
        return s.length() <= limit ? s : s.substring(0, limit);
    }

    private static int extractInt(String text, String regex) {
        if (text == null) return 0;
        var m = java.util.regex.Pattern.compile(regex).matcher(text);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception ignored) {}
        }
        return 0;
    }

    private static int extractAny(String text, String... regexes) {
        for (String r : regexes) {
            int v = extractInt(text, r);
            if (v != 0) return v;
        }
        return 0;
    }

    private static void deleteRecursively(Path p) throws IOException {
        if (!Files.exists(p)) return;
        Files.walk(p)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try { Files.deleteIfExists(path); } catch (IOException ignored) {}
                });
    }
}