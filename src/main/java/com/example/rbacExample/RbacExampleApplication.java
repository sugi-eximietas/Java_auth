package com.example.rbacExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ComponentScan("com.example.rbacExample")
@EnableWebSecurity
public class RbacExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RbacExampleApplication.class, args);
		System.out.println("XDXDXDXD");
	}

}
