package com.example;

public class Demo {

	
	public void m1(Object o)
	{
		System.out.println(" m1 Object Will Call");
	}
	
	public void m1(String s)
	{
		System.out.println(" m1 String Will Call");
	}
	
	public void m1(Demo d1)
	{
		System.out.println("Demo Will Call");
	}
	
	public static void main(String[] args) {
		Demo d=new Demo();
		//d.m1(null);
		d.m1((Object)null);
		d.m1((String)null);
		d.m1((Demo)null);
		
		
		
		
	}
}
