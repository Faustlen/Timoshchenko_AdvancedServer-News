package ibs.news.config;

import ibs.news.security.AuthenticationEntryPointImpl;
import ibs.news.security.JwtAuthenticationFilter;
import ibs.news.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
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
                                "v1/auth/register", "v1/auth/login", "v1/file/uploadFile").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "v1/user/{id}", "v1/news", "v1/file/{fileName}").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "v1/user/info", "v1/user", "v1/news/user/{userId}").authenticated()
                        .requestMatchers(HttpMethod.PUT,
                                "v1/user", "v1/news/find", "v1/news/{newsId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,
                                "v1/user", "v1/news/{newsId}").authenticated()
                        .requestMatchers(HttpMethod.POST,
                                "v1/news").authenticated()
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
