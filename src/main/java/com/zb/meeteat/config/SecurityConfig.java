package com.zb.meeteat.config;

import com.zb.meeteat.jwt.JwtFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 보안 설정을 위한 클래스 비밀번호 암호화를 위해 BCryptPasswordEncoder를 빈으로 등록
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;

  /**
   * 비밀번호를 안전하게 암호화하기 위한 PasswordEncoder 빈 등록
   *
   * @return BCryptPasswordEncoder 인스턴스
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(HttpMethod.POST, "/api/users/signup", "/api/users/signin",
//                    "/api/users/signin/*", "api/restaurants/search")
//                .permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/restaurants/{restaurantId}",
//                    "/api/restaurants/{restaurantId}/reviews").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/users/change-password").authenticated() // 인증 필요
//                .anyRequest().authenticated()
                .requestMatchers("/**").permitAll() //모든 요청 허용 (테스트용)
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

    return http.build();
  }

}
