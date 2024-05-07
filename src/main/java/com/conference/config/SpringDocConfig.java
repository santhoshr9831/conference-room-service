package com.conference.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
        .info(
            new Info()
                .title("conference-room-service")
                .version("1.0.0")
                .description("This service do not need any authentication." +
                        "It is connect to H2 DB and its console can be access from http://localhost:8080/h2. \n" +
                        "Refer README.md for more information"));
  }
}
