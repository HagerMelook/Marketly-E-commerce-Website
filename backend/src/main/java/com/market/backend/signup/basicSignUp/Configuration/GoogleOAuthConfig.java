package com.market.backend.signup.basicSignUp.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class GoogleOAuthConfig {

    @Bean
    public SecurityFilterChain customizeSecurityFilterChain(HttpSecurity http) throws Exception {
//        Customizing the filter chain to handle different types of auths and apply web security

        http
            .authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/SignUp/Google/**").authenticated()  // Require google OAuth for this url
                .anyRequest().permitAll()  // Don't require auth for the rest
//                .anyRequest().authenticated()  // Don't require auth for the rest
            )
            .logout(logout -> logout
//                    .logoutSuccessUrl("/")  // Redirect after logout
                    .invalidateHttpSession(true)  // Invalidate session
                    .deleteCookies("JSESSIONID")  // Delete session cookies
            )
            .csrf(csrf -> csrf
                    .disable()
//                    .ignoringRequestMatchers("/SignUp/Google/Client", "/SignUp/ClientBasicSignUp")
            )
            .cors(cors -> cors.
                    configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))  // Enable CORS globally

                .oauth2Login(Customizer.withDefaults()) //Specifically require Google oauth2
        ;

//        For Later Milestones:
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login")  // Custom login page for later phases
//                        .defaultSuccessUrl("/home", true)  // Redirect to home after successful login
//                )
//                .logout(logout -> logout.logoutSuccessUrl("/"))  // Redirect to home page after logout

        return http.build();
    }

}
