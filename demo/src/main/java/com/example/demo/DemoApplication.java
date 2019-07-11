package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com")
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Hiii");
		SpringApplication.run(DemoApplication.class, args);
	}
}
