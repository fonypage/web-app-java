package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.EmailLoginCode;
import com.app.java.WebAppJava.repository.EmailLoginCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class EmailOtpService {

    private final EmailLoginCodeRepository repo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Random rnd = new Random();

    @Value("${app.otp.ttl-minutes:5}")
    private int ttlMinutes;

    @Value("${app.otp.cooldown-seconds:30}")
    private int cooldownSeconds;

    @Value("${app.otp.max-attempts:5}")
    private int maxAttempts;

    private final MailSenderService mailSenderService;

    public EmailOtpService(EmailLoginCodeRepository repo, MailSenderService mailSenderService) {
        this.repo = repo;
        this.mailSenderService = mailSenderService;
    }

    @Transactional
    public void requestCode(String email) {
        email = normalize(email);
        LocalDateTime now = LocalDateTime.now();

        List<EmailLoginCode> active = repo.findActive(email, now);
        if (!active.isEmpty()) {
            EmailLoginCode last = active.get(0);
            if (last.getLastSentAt() != null && last.getLastSentAt().isAfter(now.minusSeconds(cooldownSeconds))) {
                // Не спамим кодами. Для UX — считаем что “код отправлен”.
                return;
            }
        }

        String code = String.format("%06d", rnd.nextInt(1_000_000));

        EmailLoginCode rec = new EmailLoginCode();
        rec.setEmail(email);
        rec.setCodeHash(encoder.encode(code));
        rec.setExpiresAt(now.plusMinutes(ttlMinutes));
        rec.setAttemptsLeft(maxAttempts);
        rec.setLastSentAt(now);
        repo.save(rec);

        // временно: печатаем в консоль, пока не подключили Brevo
        mailSenderService.sendLoginCode(email, code, ttlMinutes);
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        email = normalize(email);
        code = code == null ? "" : code.trim();
        LocalDateTime now = LocalDateTime.now();

        List<EmailLoginCode> active = repo.findActive(email, now);
        if (active.isEmpty()) return false;

        EmailLoginCode rec = active.get(0);
        if (rec.getAttemptsLeft() <= 0) return false;

        boolean ok = encoder.matches(code, rec.getCodeHash());
        rec.setAttemptsLeft(rec.getAttemptsLeft() - 1);

        if (ok) rec.setUsedAt(now);
        repo.save(rec);

        return ok;
    }

    private static String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}