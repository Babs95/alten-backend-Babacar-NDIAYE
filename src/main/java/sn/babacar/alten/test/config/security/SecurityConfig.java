package sn.babacar.alten.test.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;


@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
  private static final String[] WHITELIST = {
      "/account",
      "/token",
      "/swagger-ui/**",
      "/v3/api-docs/**"
  };

  private final JwtRequestFilter jwtRequestFilter;

  @Value("${alten.endpoints.frontend}")
  private String frontEndUrl;

  @Autowired
  public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      CustomAuthenticationEntryPoint entryPoint,
      CustomAccessDenied accessDenied) throws Exception {

    http
        .httpBasic(withDefaults())
        .formLogin(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .exceptionHandling(exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDenied)
        )
        .authorizeHttpRequests(authorize -> {
          // Chemins en liste blanche autorisés sans authentification
          authorize.requestMatchers(request -> {
            for (String pattern : WHITELIST) {
              if (new AntPathRequestMatcher(pattern).matches(request)) {
                return true;
              }
            }
            return false;
          }).permitAll();

          // Autorisation spécifique pour les opérations sur les produits
          authorize.requestMatchers(HttpMethod.GET, "/v1/products/**").authenticated();
          authorize.requestMatchers(HttpMethod.POST, "/v1/products").hasRole("ADMIN");
          authorize.requestMatchers(HttpMethod.PATCH, "/v1/products/**").hasRole("ADMIN");
          authorize.requestMatchers(HttpMethod.DELETE, "/v1/products/**").hasRole("ADMIN");

          // Tous les autres chemins nécessitent une authentification
          authorize.anyRequest().authenticated();
        })
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    List<String> allowedOrigins = Arrays.stream(frontEndUrl.split(",")).map(String::trim).toList();

    log.info("allowedOrigins {}", allowedOrigins);
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(allowedOrigins);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(
        List.of("Authorization", "Cache-Control", "Content-Type", "X-JWT-Assertion"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}