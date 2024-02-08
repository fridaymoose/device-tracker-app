package ovh.kg4.devicetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Value("${spring.cors.max.age:3600}")
  private Long maxAge;

  @Value("${spring.cors.origins:127.0.0.1,localhost}")
  private String[] origins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**")
      .maxAge(maxAge)
      .allowedOrigins(origins);
  }
}
