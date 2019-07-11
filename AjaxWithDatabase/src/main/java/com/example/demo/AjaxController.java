package com.example.demo;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {
	
	@Autowired
	CrudAjax ca;
	@RequestMapping("/")
	public String m1()
	{
		return "index";
		
	}
	@RequestMapping("/success")
	@ResponseBody
	public List<Stud> m2(@RequestParam String d,Model m)
	{
		List<Stud>list=(List<Stud>)ca.findAll();
		m.addAttribute("msg",list);
		return list;	
	}
}
