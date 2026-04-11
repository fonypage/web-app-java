package com.app.java.WebAppJava.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // public pages
                .antMatchers(
                        "/", "/theory", "/practice", "/topic/**",
                        "/images/**",
                        "/login-user", "/login-user-code",
                        "/auth/email/**"
                ).permitAll()

                // user area
                .antMatchers("/test/**", "/test-result", "/my-tests", "/my-tests/**", "/practice/**")
                .hasAnyRole("USER","ADMIN")

                .anyRequest().authenticated()
                .and()

                // вместо formLogin делаем редирект на /login-user, если не авторизован
                .exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> res.sendRedirect("/login-user"))
                .and()

                .logout()
                .logoutUrl("/logout-user")
                .logoutSuccessUrl("/")
                .permitAll();
    }
}