package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String m1()
	{
		return "index";
	}
	
	
	@RequestMapping("/log")
	@ResponseBody
	public String m2(@RequestParam String d)
	{
		System.out.println(d);
		return d;
	}
	 
}
