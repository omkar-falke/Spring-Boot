package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AjaxWithDatabaseApplication {

	public static void main(String[] args) {
		
		System.out.println("Hello Db Code");
		SpringApplication.run(AjaxWithDatabaseApplication.class, args);
	}

}
