package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeExample {

	
	@RequestMapping("/log")
	public String m1()
	{
		return "Hello Guys";	
		
	}
	
}
