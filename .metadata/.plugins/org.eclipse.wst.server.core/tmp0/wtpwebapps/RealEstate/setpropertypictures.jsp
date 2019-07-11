<%@page import="java.util.List"%>
<%@page import="entities.PropertyPhotos"%>
<%@page import="service.PropertyIO"%>
<%@page import="entities.Property"%>
<%@page import="entities.User"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - Add Property Photos</title>	
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
                            response.sendRedirect("index.jsp");
                        } else {%>
                    <div id='cssmenu'>
                        <ul>
                            <li><a href='#'><%=user.getName()%></a>
                                <ul>
                                    <li><a href='myproperties.jsp'>My Properties</a></li>
                                    <li><a href='editprofile.jsp'>Edit Profile</a></li>
                                    <li><a href='logout.jsp'>Logout</a></li>
                                </ul></li>
                                <%if (user.isLessor() || user.isSeller()) {%>
                            <li><a href='addproperty.jsp'>Property Registration</a></li>
                                <%}%>
                            <li><a href='search.jsp'>Property Search</a></li>
                        </ul>
                    </div>
                    <%}%>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <h1>Change Ownership Photos</h1>
                    <%
                        String userId = (String) session.getAttribute("uid");
                        String pid = (String) session.getAttribute("pid");
                        List<PropertyPhotos> photos = PropertyIO.getPropertyPhotos(Integer.parseInt(pid));
                    %>
                </div>
            </div>
            <%
                if (photos.size() != 0) {
            %>
            <div class="onerow">
                <div class="col12 last">
                    <label>Select Files <br/><font color="red">(maximum size: 1 megabyte / maximum number of files: 5 / allowed file type: jpg)</font>
                    </label>
                    <form id="general-form" action="setpropertypictures?type2=set&amp;username=<%=user.getUsername()%>&amp;pid=<%=pid%>" method="POST" enctype="multipart/form-data" onsubmit="return validateImages(this)">
                        <%
                            int i = 1;
                            for (PropertyPhotos p : photos) {
                                int z = Character.getNumericValue(p.getPath().substring(p.getPath().lastIndexOf("e")).charAt(1));

                        %>
                        <input hidden id="picture<%=z%>" type="file" name="picture<%=z%>"/>
                        <p><img id="pp" src="images/propertiespictures/property_picture<%=z%>_us<%=user.getUserId()%>_pr<%=pid%>.jpg" alt=""/>
                            <br/><a href="deletepropertypicture?i=<%=z%>&amp;uid=<%=user.getUserId()%>&amp;pid=<%=pid%>">Delete Photo</a></p>
                            <%i++;
                                }%>
                        <p class="general">
                        </p>
                        <%
                            for (int j = i; j <= 5; j++) {
                        %>
                        <p><input id="picture<%=j%>" type="file" name="picture<%=j%>"/></p>
                            <%}%>
                        <p><input class="buttom" name="submit" id="submit" value="Integration" type="submit"> </p>
                    </form>
                </div>
            </div>
            <%} else {%>
            <div class="onerow">
                <div class="col12 last">
                    <%if (request.getHeader("Referer").contains("editproperty") || request.getHeader("Referer").contains("setpropertypictures")) {%>
                    <form id="general-form" action="setpropertypictures?type2=set&amp;username=<%=user.getUsername()%>&pid=<%=pid%>" method="POST" enctype="multipart/form-data" onsubmit="return validateImages(this)">
                        <%} else {%>
                        <form id="general-form" action="setpropertypictures?type2=suc&amp;username=<%=user.getUsername()%>&pid=<%=pid%>" method="POST" enctype="multipart/form-data" onsubmit="return validateImages(this)">
                            <%}%>
                            <p class="general"><label>Select Files <font color="red">(maximum size: 1 megabyte / maximum number of files: 5 / allowed file type: jpg)</font>
                                </label></p>
                            <p> <input id="picture1" type="file" name="picture1" /></p>
                            <p><input id="picture2" type="file" name="picture2" /></p>
                            <p><input id="picture3" type="file" name="picture3" /></p>
                            <p><input id="picture4" type="file" name="picture4" /></p>
                            <p><input id="picture5" type="file" name="picture5" /></p>
                            <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>
                        </form>
                </div>
            </div> 
            <%}%>
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
