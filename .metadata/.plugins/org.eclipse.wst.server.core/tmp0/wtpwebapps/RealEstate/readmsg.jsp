<%@page import="entities.Messages"%>
<%@page import="service.UserIO"%>
<%@page import="entities.PropertyPhotos"%>
<%@page import="entities.Property"%>
<%@page import="entities.Property"%>
<%@page import="service.PropertyIO"%>
<%@page import="java.util.Locale"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="entities.User"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - View Property Message</title>	
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            String uid = (String) request.getParameter("uid");
            String mid = (String) request.getParameter("mid");
            String pid = (String) request.getParameter("pid");
            List<Messages> msglst = PropertyIO.getmsgs(Integer.parseInt(uid), Integer.parseInt(pid));
            Messages msg = null;
            for (Messages m : msglst) {
                if (m.getId().getMessageId() == Integer.parseInt(mid)) {
                    msg = m;
                    break;
                }
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
                                    <%if (user.isLessor() || user.isSeller()) {%>
                                    <li><a href='myproperties.jsp'>My Properties</a></li> <%}%>
                                    <li><a href='editprofile.jsp'>Edit Profile</a></li>
                                    <li><a href='logout.jsp'>Logout</a></li>
                                </ul></li>
                                <%if (user.isLessor() || user.isSeller()) {%>
                            <li><a href='addproperty.jsp'>Property Registration</a></li>
                                <%}%>
                            <li><a href='search.jsp'>Property Search</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last" >
                    <h1>View a Message</h1> </div> </div>
            <div class="onerow">
                <div class="col12 last" >
                    <p><%=msg.getMessage()%></p>
                    <p>
                        <a href="propertyinfo.jsp?userid=<%=uid%>&amp;pid=<%=pid%>">Return</a><br/>
                        <a href="deletemsg?userid=<%=uid%>&amp;pid=<%=pid%>&amp;mid=<%=mid%>">Delete massage</a> 
                    </p>
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