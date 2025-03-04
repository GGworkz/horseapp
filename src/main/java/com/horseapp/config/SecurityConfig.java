//package com.horseapp.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.http.SessionCreationPolicy;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/user/signup", "/user/signin", "/client/signup", "/client/signin").permitAll() // Public endpoints
//                        .anyRequest().authenticated() // Protect all other routes
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session only when needed
//                )
//                .formLogin(form -> form // Explicitly configure form login
//                        .loginPage("/user/signin") // Define login endpoint (optional)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/user/logout")
//                        .logoutSuccessUrl("/user/signin")
//                );
//
//        return http.build();
//    }
//}
