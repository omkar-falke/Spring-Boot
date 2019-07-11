package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class HomeController {
	@RequestMapping("/")
	public String m1()
	{
		return "index";	
	}
	
	@RequestMapping("/log")
	@ResponseBody
	public String m2()
	{
			Student s=new Student();
			s.setRoll(101);
			s.setName("om");
			s.setAddress("pune");
			
			Gson g=new Gson();
			String json=g.toJson(s);
			System.out.println(json);
			return json;
		
	}

}
