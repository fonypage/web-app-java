package com.app.java.WebAppJava.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 1. Конфиг для админки (авторизация ADMIN по /admin/**, страница /login)
@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/admin/**","/login","/logout").and()
                .authorizeRequests()
                .antMatchers("/login","/logout").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("ADMIN");
    }
}

