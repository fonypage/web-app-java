package com.app.java.WebAppJava.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 2. Конфиг для простых пользователей (все остальные защищённые урлы, страница /login-user)
@Configuration
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/", "/theory", "/practice", "/topic/**",
                            "/images/**").permitAll()
                    .antMatchers("/test/**","/test-result","/my-tests")
                    .hasAnyRole("USER","ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login-user")
                    .defaultSuccessUrl("/my-tests", true)
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout-user")
                    .logoutSuccessUrl("/")
                    .permitAll();
        }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // например, можно завести in-memory ещё одного юзера
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER");
    }
}

