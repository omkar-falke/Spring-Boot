<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="javax.script.Invocable"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="javax.script.ScriptEngine"%>
<%@page import="javax.script.ScriptEngineManager"%>
<%@page import="service.UserIO"%>
<%@page import="java.util.List"%>
<%@page import="entities.User"%>
<%@page import="misc.IP_Functions"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/style.css" rel="stylesheet" type="text/css"/>
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <script>countdownregister(10);</script>
        <title>Find A Residence - Registration problem</title>	
    </head>
    <body> 
        <%
            User user = (User) session.getAttribute("user");
            if (user != null) {
                response.sendRedirect("index.jsp");
            }
        %>
        <div class="onepcssgrid-1200">
            <div class="onerow">
                <div class="col5">
                    <a href="index.jsp"><img src="images/far.png" alt="logo"/></a>
                </div>
                <div class="col7 last">
                    <div id='cssmenu'>
                        <ul>
                            <li><a href='register.jsp'>Sign up</a></li>
                            <li onclick="popup('login')"><a href='#'>Login</a></li>
                            <li><a href='search.jsp'>Property Search</a></li>
                        </ul>
                    </div>
                </div>
                <div id="blanket" style="display:none" onclick="popup('login')"></div>
                <div id="login" style="display:none;">
                    <button type="button" onclick="popup('login')">x</button>
                    <form id="login-form" action="login">
                        <input type="text" placeholder="Username" name="username"/>
                        <input type="password" placeholder="Password" name="password" />
                        <input type="submit" value="Login" />
                    </form>
                </div>
            </div>
            <div class="onerow">
                <div class="col2"></div>
                <div class="col8">
                    <p>An error occurred during the registration process. <br>
                        Please try again.
                    </p>
                    <p>
                        Stay <span id="countdownregister">10 seconds</span> to redirect to 
                        <a href="register.jsp"> Sign up</a>.</p>
                </div>
                <div class="col2 last"></div>
            </div>
            <div class="onerow">
                <div class="col1">             
                    <a href="index.jsp"><img src="images/farfooter.png" alt="logo"/></a>
                </div>
                <div class="col3"><p><b>Find A Residence &copy;</b><br>Support for Rent / Purchase Decisions</p></div>
                <div class="col4">             
                    <p><b>Powered by:</b><br/>Infoideal technologies - 9812345648<br/>Office- 9812345648</p>
                </div>
                <div class="col4 last">             
                    <p><a href="mailto:akashwaghmare54@gmail.com?Subject=Find A Residence" target="_top">Contact us</a></p>
                </div>
            </div> 
        </div>
    </body>
</html>