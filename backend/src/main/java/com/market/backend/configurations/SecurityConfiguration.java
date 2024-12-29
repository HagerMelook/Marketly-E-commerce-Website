package com.market.backend.configurations;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Enforce
                                                                                                              // stateless
                                                                                                              // sessions
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore((Filter) jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll() 
                        .requestMatchers("/categories/**").hasRole("SUPERADMIN") 
                        .anyRequest().authenticated()
                        .requestMatchers("/SignUp/Google/**").authenticated() // Require google OAuth for this url
                        .anyRequest().authenticated() // Secure all other APIs
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOrigin("http://localhost:5173"); // Allow frontend
                    configuration.addAllowedMethod("*"); // Allow all HTTP methods
                    configuration.addAllowedHeader("*"); // Allow all headers
                    configuration.setAllowCredentials(true); // Allow cookies
                    return configuration;
                })) // Enable CORS globally
                .logout(logout -> logout.logoutSuccessUrl("/"))
                // Redirect after logout, no need to delete session ids
                // as we using statless session mangment policy
                .build();
    }

    @Bean
    public UserDetailsService userDetailsManager(DataSource dataSource) {
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder encoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
}
