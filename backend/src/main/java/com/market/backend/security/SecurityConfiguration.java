package com.market.backend.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(HttpMethod.POST, "/auth/signin").hasRole("USER")

                // .requestMatchers(HttpMethod.GET, "/auth/signin").hasRole("ADMIN")
                )
                // .sessionManagement(session ->
                // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
            "SELECT a.username, p.account_password, a.status AS enabled " +
            "FROM account a " +
            "JOIN password p ON a.account_id = p.account_id " +
            "WHERE a.username = ?");


        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
            "SELECT a.username, a.type AS role " +
            "FROM account a " +
            "WHERE a.username = ?");

        return jdbcUserDetailsManager;
    }
}
