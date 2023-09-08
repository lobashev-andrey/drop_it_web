package org.example.app.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = Logger.getLogger(AppSecurityConfig.class);

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("populate inmemory auth user");
        auth
                .inMemoryAuthentication()
                .withUser("root")
                .password(passwordEncoder().encode("123"))
                .roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){   // Для шифрования пароля
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("config http security");
        http.headers().frameOptions().disable(); //
        http
                .csrf().disable() // кросс-сайт референс форджери отключен. Вообще этого делать не надо
                .authorizeRequests()                           //  авторизовать запросы,
                .antMatchers("/login*").permitAll() // начинающиеся с /login
                .anyRequest().authenticated() // все запросы должны проходить аутентификацию
                .and()
                .formLogin()         // предоставляем нашу собственную форму логина
                .loginPage("/login") // на нашей собственной странице логина
                .loginProcessingUrl("/login/auth")// процесс, который обрабатывает аутентификацию
                .defaultSuccessUrl("/books/shelf", true) // страница по умолчанию при успехе
                .failureUrl("/login"); // при неуспехе
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        logger.info("config web security");
        web
                .ignoring()                             // игнорируем начинающиеся
                .antMatchers("/images/**");  // с паттерна /images..
        // это чтобы security пропускала обращения нашего приложения к статическим ресурсам.






    }
}
