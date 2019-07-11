<%@page import="com.example.demo.Stud"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success</title>
</head>
<body>

<%
List<Stud> li=(List<Stud>)request.getAttribute("msg");

for(Stud s:li)
{
%>
<%=s.getId() %>
<%} %>
</body>
</html>