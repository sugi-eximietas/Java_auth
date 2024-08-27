package com.example.sdkotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
// import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@ComponentScan("com.example.sdkotel")
public class SdkotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdkotelApplication.class, args);
		// System.out.println("helloooo");
		// Dotenv.configure().directory("C:\\Users\\Sugavanaesh S\\Documents\\sdkotel\\.env").load();
	}
	@Bean
	public OpenTelemetry openTelemetry() {
	  return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
	}

}
