<%@page import="entities.User"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - Change User Photo</title>	
    </head>
    <body>
        <div class="onepcssgrid-1200">
            <div class="onerow">
                <div class="col5">
                    <a href="index.jsp"><img src="images/far.png" alt="logo"/></a>
                </div>
                <div class="col7 last">
                    <% User user = (User) session.getAttribute("user");
                        if (user == null) {
                    %>
                    <div id='cssmenu'>
                        <ul>
                            <li><a href='register.jsp'>Sign up</a></li>
                            <li onclick="popup('login')"><a href='#'>Login</a></li>
                            <li><a href='search.jsp'>Property Search</a></li>
                        </ul>
                    </div>
                    <%} else {%>
                    <div id='cssmenu'>
                        <ul>
                            <li><a href='#'><%=user.getName()%></a>
                                <ul>
                                    <li><a href='myproperties.jsp'>My Properties</a></li>
                                    <li><a href='editprofile.jsp'>Edit Profile</a></li>
                                    <li><a href='logout.jsp'>Logout</a></li>
                                </ul></li>
                                <%if (user.isLessor() || user.isSeller()) {%>
                            <li><a href='#'>Property Registration</a></li>
                                <%}%>
                            <li><a href='search.jsp'>Property Search</a></li>
                        </ul>
                    </div>
                    <%}%>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <h1>Change User Photo</h1>
                    <% if (user != null) {%>
                    <img id="pp" src="images/profilepictures/profile_picture_<%= user.getUserId()%>.jpg" alt=""/>
                    <%}%>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <form id="general-form" action="setprofilepicture" method="POST" enctype="multipart/form-data" onsubmit="return validateImage(this)">
                        <%
                            String username;
                            if (user == null) {
                                username = (String) request.getAttribute("username");
                                if (username == null) {
                                    response.sendRedirect("index.jsp");
                                }
                            } else {
                                username = user.getUsername();
                            }
                            out.println("<input hidden type=\"text\" name=\"username\" value=\"" + username + "\"/>");%>
                        <p class="general"><label for="picture">File selection <font color="red">(maximum size: 1 megabyte)</font>
                            </label></p>
                        <input id="picture" type="file" name="picture" />
                        <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>	 
                    </form>
                    <%if (user == null) {%>
                    <p><a href="pending.jsp">Skip</a></p>	 
                    <% } else { %>
                    <p><a href="editprofile.jsp">Return</a></p><%}%>	 
                </div>
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
