package com.contraflow.cms.security.config;


import com.contraflow.cms.security.jwt.JwtAuthenticationFilter;
import com.contraflow.cms.security.service.CustomUserDetailsService;
import com.contraflow.cms.security.service.TenantUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())   // enable CORS using the corsConfigurationSource bean below
                .csrf(csrf -> csrf.disable())   // typical for a stateless JSON/JWT API
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/auth/otp","/auth/validate","/auth/reset").permitAll()   // <-- NOT "/api/v1/auth/**"
                        .requestMatchers("/health").permitAll()    // public liveness check (Railway healthcheck)
                        // Swagger / OpenAPI docs — public (paths are relative to the /api/v1 servlet path)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    // Dev React apps on any localhost port (3000, 5173, etc.). Add your prod origin here later.
    config.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization"));
    config.setAllowCredentials(true);   // allow Authorization header / cookies
    config.setMaxAge(3600L);            // cache preflight for 1 hour

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}


@Bean
public PasswordEncoder passwordEncoder(){

    return new BCryptPasswordEncoder();
}


@Bean
@Primary
public AuthenticationManager authenticationManager(CustomUserDetailsService customUserDetailsService,
                                                   PasswordEncoder passwordEncoder){
    // Build an explicit ProviderManager for the ADMIN UserDetailsService.
    // (Using AuthenticationConfiguration.getAuthenticationManager() with MORE THAN ONE
    // UserDetailsService bean produces a manager that delegates to itself -> StackOverflow.)
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
}


@Bean
public AuthenticationManager tenantAuthenticationManager(TenantUserDetailsService tenantUserDetailsService,
                                                         PasswordEncoder passwordEncoder){
    // Separate manager bound to the TENANT UserDetailsService (tenant_user table).
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(tenantUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
}


}
