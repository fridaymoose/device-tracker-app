package ovh.kg4.devicetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
@EnableRetry
@EnableScheduling
public class AppConfig {

  @Value("${spring.rest.template.connection.timeout.sec:20}")
  private Integer connectionTimeout;

  @Value("${spring.rest.template.read.timeout.min:2}")
  private Integer readTimeout;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
      .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
      .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
      .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
      .setReadTimeout(Duration.ofMinutes(readTimeout))
      .build();
  }
}
