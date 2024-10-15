package ibs.news.config;

import ibs.news.security.AuthenticationEntryPointImpl;
import ibs.news.security.JwtAuthenticationFilter;
import ibs.news.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(man -> man.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "auth/register", "auth/login", "file/uploadFile").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "user/{id}", "news", "file/{fileName}").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "user/info", "user", "news/user/{userId}").authenticated()
                        .requestMatchers(HttpMethod.PUT,
                                "user", "news/find", "news/{newsId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,
                                "user", "news/{newsId}").authenticated()
                        .requestMatchers(HttpMethod.POST,
                                "news").authenticated()
                )
                .exceptionHandling(config -> config.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
