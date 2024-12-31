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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private JWTFilter jwtFilter;

    public SecurityConfiguration(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(customizer -> customizer.disable())
                .headers(headers -> headers.frameOptions().disable()) // Add this for H2 console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore((Filter) jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/h2-console/**").permitAll() // Add this for H2 console

                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("SUPERADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/{categoryName}").hasRole("SUPERADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/{categoryName}").hasRole("SUPERADMIN")

                        // Admin Controller APIs
                        .requestMatchers(HttpMethod.GET, "/admin/info/{username}").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/admin/activate/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/admin/deactivate/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/delete/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/admin/promote/{id}").hasRole("SUPERADMIN")
                        .requestMatchers(HttpMethod.PUT, "/admin/demote/{id}").hasRole("SUPERADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/feedback").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/feedback/{feedbackId}")
                        .hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/request").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/request/accept/{requestId}")
                        .hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/request/delete/{requestId}")
                        .hasAnyRole("SUPERADMIN", "ADMIN")

                        // Search Controller API
                        // already available for all users

                        // SignUp Controller APIs
                        .requestMatchers(HttpMethod.GET, "/SignUp/Google/Client").permitAll()
                        .requestMatchers(HttpMethod.GET, "/SignUp/Google/Vendor/{org}/{tax}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/SignUp/ClientBasicSignUp/{password}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/SignUp/VendorBasicSignUp").permitAll()

                        // SignIn Controller API
                        .requestMatchers(HttpMethod.GET, "/auth/signin/google").permitAll()

                        // Comments Controller API
                        // already available for all users

                        // Edit Profile Controller API
                        .requestMatchers(HttpMethod.GET, "/account/admininfo/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/account/clientinfo/{id}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/account/vendorinfo/{id}").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.PATCH, "/account/admininfo/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/account/clientinfo/{id}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/account/vendorinfo/{id}").hasRole("VENDOR")

                        // Product Page Controller API
                        // already available for all users

                        // Rates Controller API
                        // already available for all users

                        // Vendor Controller API
                        .requestMatchers(HttpMethod.GET, "/vendor/categories").hasRole("VENDOR")

                        .anyRequest().authenticated()

                )
                .oauth2Login(Customizer.withDefaults())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOrigin("http://localhost:5173");
                    configuration.addAllowedMethod("*");
                    configuration.addAllowedHeader("*");
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT a.username, p.account_password, status AS enabled " +
                        "FROM account a " +
                        "JOIN password p ON a.account_id = p.account_id " +
                        "WHERE a.username = ?");

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT username, type AS role " +
                        "FROM account  " +
                        "WHERE username = ?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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