package com.example.unirideapi.config;

import com.example.unirideapi.security.JWTAuthenticationEntryPoint;
import com.example.unirideapi.security.JWTConfigurer;
import com.example.unirideapi.security.JWTFilter;
import com.example.unirideapi.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // para @PreAuthorize, etc.
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JWTFilter jwtRequestFilter;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CORS permitido con defaults
                .cors(Customizer.withDefaults())

                // Desactivar CSRF porque estamos haciendo API stateless con JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (no requieren token)
                        .requestMatchers(
                                "/auth/login",
                                "/auth/registro/pasajero",
                                "/auth/registro/conductor",
                                "/conductores/recent",
                                "/pasajeros/recent",
                                "/webjars/**",
                                //Permisos para swagger
                                "/api/v1/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()

                        // Todo lo demás sí requiere estar autenticado
                        .anyRequest().authenticated()
                )

                // Manejo de errores de auth -> tu entrypoint JWT (401 Unauthorized limpio)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // Sin sesión de servidor, todo stateless via JWT
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Aplica tu config adicional de JWT (si JWTConfigurer mete más filtros, etc.)
                .with(new JWTConfigurer(tokenProvider), Customizer.withDefaults());

        // Registramos tu filtro JWT ANTES que el filtro de UsernamePasswordAuthenticationFilter,
        // para que el token se valide en cada request antes de llegar a los controladores.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
