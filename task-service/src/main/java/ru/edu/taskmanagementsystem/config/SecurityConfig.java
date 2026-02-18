package ru.edu.taskmanagementsystem.config;

import ru.edu.taskmanagementsystem.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Включаем CORS с нашей конфигурацией
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Отключаем CSRF для API
            .csrf(AbstractHttpConfigurer::disable)
            // Настройка авторизации
            .authorizeHttpRequests(authz -> authz
                // Разрешаем OPTIONS запросы для всех URL без аутентификации
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Разрешаем доступ к эндпоинтам аутентификации
                .requestMatchers("/auth/**", "/login", "/register").permitAll()
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            // Отключаем form login, так как используем JWT
            .formLogin(AbstractHttpConfigurer::disable)
            // Отключаем logout
            .logout(AbstractHttpConfigurer::disable)
            // Добавляем JWT фильтр
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Разрешаем запросы с фронтенда
        configuration.setAllowedOrigins(List.of("http://localhost:8084"));
        
        // Разрешаем все необходимые методы
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Разрешаем все заголовки
        configuration.setAllowedHeaders(List.of("*"));
        
        // Разрешаем отправку куки/авторизационных данных
        configuration.setAllowCredentials(true);
        
        // Указываем, какие заголовки можно читать
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        
        // Кэшируем preflight ответ на 1 час
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());

        return delegatingPasswordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}