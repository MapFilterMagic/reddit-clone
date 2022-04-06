package com.mapfiltermagic.springredditclone.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO: Remove once confirmed what's needed and what's not. Using this leads to 403's.
    // @Override
    // public void configure(HttpSecurity httpSecurity) throws Exception {
    //     httpSecurity.cors().and()
    //             .csrf().disable()
    //             .authorizeHttpRequests(authorize -> authorize
    //                     .antMatchers("/api/auth/**")
    //                     .permitAll()
    //                     .antMatchers(HttpMethod.GET, "/api/subreddit")
    //                     .permitAll()
    //                     .antMatchers(HttpMethod.GET, "/api/posts/")
    //                     .permitAll()
    //                     .antMatchers(HttpMethod.GET, "/api/posts/**")
    //                     .permitAll()
    //                     .antMatchers("/v2/api-docs",
    //                             "/configuration/ui",
    //                             "/swagger-resources/**",
    //                             "/configuration/security",
    //                             "/swagger-ui.html",
    //                             "/webjars/**")
    //                     .permitAll()
    //                     .anyRequest()
    //                     .authenticated()
    //             );
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
