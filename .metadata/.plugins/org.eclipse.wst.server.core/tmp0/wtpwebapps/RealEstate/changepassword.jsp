<%@page import="entities.User"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - Change Password</title>	
    </head>
    <body>
        <% User user = (User) session.getAttribute("user");
            if (user == null) {
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
                            <li><a href='#'><%=user.getName()%></a>
                                <ul>
                                    <li><a href='myproperties.jsp'>My Properties;</a></li>
                                    <li><a href='editprofile.jsp'>Edit Profile</a></li>
                                    <li><a href='logout.jsp'>Logout</a></li>
                                </ul></li>
                                <%if (user.isLessor() || user.isSeller()) {%>
                            <li><a href='#'>Property Registration</a></li>
                                <%}%>
                            <li><a href='search.jsp'>Property Search</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <h1>Change Password</h1>
                    <form id="general-form" name="form" method="post" action="changepassword" onsubmit="return validateChangePasswordForm()">
                        <p class="general">
                            <label>Old Password <font color="red">*</font>
                            </label></p>
                        <input type="password" name="oldpassword"  />            
                        <p class="general">
                            <label>New Password <font color="red">*</font>
                            </label></p>
                        <input type="password" name="newpassword"  />

                        <p class="general">
                            <label>Confirm New Password <font color="red">*</font>
                            </label></p>
                        <input type="password" name="renewpassword"  />
                        <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>  
                    </form>
                    <p>(<font color="red">*</font> Obligatory field)</p>
                        <%String message = (String) request.getAttribute("status");
                        if (message != null && message.equals("success")) {%>
                    <p style="color: green">Code changed successfully!</p>
                    <%} else if (message != null && message.equals("fail")) {%>
                    <p style="color: red">You entered the wrong password. Please try again.</p>
                    <%}%>
                    <p><a href="editprofile.jsp">Return</a></p>
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
