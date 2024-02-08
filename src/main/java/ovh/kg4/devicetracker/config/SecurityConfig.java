package ovh.kg4.devicetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private static final String[] AUTH_WHITELIST = {
    // -- Swagger UI v2
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui.html",
    "/webjars/**",
    // -- Swagger UI v3 (OpenAPI)
    "/v3/api-docs/**",
    "/swagger-ui/**"
    // other public endpoints of your API may be appended to this array
  };

  @Value("${spring.security.default.username}")
  private String userName;

  @Value("${spring.security.default.pwd}")
  private String password;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authz ->
        authz.requestMatchers(AUTH_WHITELIST).permitAll()
          .anyRequest().authenticated())
      .httpBasic(Customizer.withDefaults());

    return httpSecurity.build();
  }

  @SuppressWarnings("deprecation")
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    // TODO: replace with robust DB backed approach
    UserDetails user = User.withDefaultPasswordEncoder()
      .username(userName)
      .password(password)
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }
}
