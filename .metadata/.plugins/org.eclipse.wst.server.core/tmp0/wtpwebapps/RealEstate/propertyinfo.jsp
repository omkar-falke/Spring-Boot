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
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&amp;sensor=false"></script>
        <script src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script>
            var map;
            var marker;
            var panPoint;
            var property = [];

            function addproperty(lt, ln) {
                property.push(lt);
                property.push(ln);
            }

            function initialize() {
                var mapOptions = {
                    zoom: 7,
                    center: new google.maps.LatLng(20.5937, 78.9629)
                };
                map = new google.maps.Map(document.getElementById('map-canvas'),
                        mapOptions);
                setmarker();
            }

            function setmarker() {
                panPoint = new google.maps.LatLng(property[0], property[1]);
                marker = new google.maps.Marker({position: panPoint, title: "Property", map: map});
                map.panTo(panPoint);
            }
            google.maps.event.addDomListener(window, 'load', initialize);</script>
        <title>Find A Residence - View Property Features</title>	
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            String uid = request.getParameter("userid");
            User owner = (User) UserIO.getUser(Integer.parseInt(uid));
            String pid = request.getParameter("pid");
            Property p = PropertyIO.getProperty(owner, owner.getUserId(), pid);
        %>		   
        <div class="onepcssgrid-1200">
            <div class="onerow">
                <div class="col5">
                    <a href="index.jsp"><img src="images/far.png" alt="logo"/></a>
                </div>
                <div class="col7 last">
                    <%
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
                        <%}%>
                    </div>
                </div>
                <div id="blanket" style="display:none" onclick="popup('login')"></div>
                <div id="login" style="display:none" >
                    <button type="button" onclick="popup('login')">x</button>
                    <form id="login-form" action="login" method="POST">
                        <input type="text" placeholder="Username" name="username"/>
                        <input type="password" placeholder="Password" name="password" />
                        <input type="submit" value="Login" />
                    </form>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last" >
                    <h1>View Property Information</h1> </div> </div>

            <div class="onerow">
                <div class="col6">
                    <p><b>Transaction Type:</b> <%=p.isForSale() ? "Sale" : "Tenancy"%></p>
                    <p><b>Type of Property:</b> <%=p.isIsApartment() ? "Apartment" : "Detached house"%></p>
                    <p><b>Manufacturing date:</b> <%=p.getBuildDate()%></p>
                    <p><b>Renovation Date:</b> <%if (p.getRenovationDate() != null) {
                            out.println(p.getRenovationDate());
                        } else {
                            out.println("-");
                        }%></p>
                    <p><b>Price: </b> <%=p.getPrice().intValue()%></p>      


                    <p><b>Maintenance costs: </b> <%=p.getMaintenanceCharges().intValue()%></p>
                    <p><b>Square meters:</b> <%=p.getSqMeters()%></p> 
                    <p><b>Number of rooms: </b> <%=p.getRoomsNo()%></p>   
                    <p><b><%=p.isIsApartment() ? "Floor" : "Floors"%>:</b> <%=p.isIsApartment() ? p.getApFloor() : p.getHFloors()%></p>             
                    <p><b>Heating system:</b> <%if (p.getHeatingSystem().equals("central_diesel")) {
                            out.print("Central Heating Oil");
                        } else if (p.getHeatingSystem().equals("atomic_diesel")) {
                            out.print("Individual Heating Oil");
                        } else if (p.getHeatingSystem().equals("central_gas")) {
                            out.print("Central Heating of Natural Gas");
                        } else if (p.getHeatingSystem().equals("atomic_gas")) {
                            out.print("Individual Natural Gas Heating");
                        } else if (p.getHeatingSystem().equals("pelet")) {
                            out.print("Heating with Pelet");
                        }
                        %></p>
                    <p><b>Air conditioning:</b> <%=p.isAirConditioner() ? "Available" : "Not available"%></p>
                    <p><b>Parking lot</b> <%=p.isParking() ? "Available" : "Not available"%></p>
                    <p><b>Elevator</b> <%=p.isElevator() ? "Available" : "Not available"%></p>
                    <p><b>Address:</b> <%=p.getAddress1()%>, <%=p.getAddress2()%> <%=p.getAddress3()%> <%=p.getPostalCode()%>,<br/> <%=p.getCity()%>, <%=p.getRegion()%> <%=p.getCountry()%></p>
                    <p><b>Description:</b> <br/><%=p.getDescription()%></p>
                    <script>addproperty(<%=p.getLatitude()%>, <%=p.getLongitude()%>, <%=p.getId().getPropertyId()%>, <%=p.getId().getOwnerUserId()%>,<%=p.isForRent()%>, <%=p.isIsApartment()%>, <%=p.getPrice()%>);</script>
                </div>
                <div class="col6 last">
                    <div id="map-canvas" style="height:500px"></div>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last" >
                    <h1>Photos</h1> </div> </div>
            <div class="onerow">
                <div class="col12 last">
                    <p>
                        <%List<PropertyPhotos> photos = PropertyIO.getPropertyPhotos(Integer.parseInt(pid));
                            for (PropertyPhotos ph : photos) {
                                int z = Character.getNumericValue(ph.getPath().substring(ph.getPath().lastIndexOf("e")).charAt(1));
                        %>
                        <a href="images/propertiespictures/property_picture<%=z%>_us<%=ph.getId().getPropertyOwnerUserId()%>_pr<%=pid%>.jpg" target="_blank"><img id="ppp" src="images/propertiespictures/property_picture<%=z%>_us<%=ph.getId().getPropertyOwnerUserId()%>_pr<%=pid%>.jpg" alt=""/></a>
                            <%}%>
                    </p>
                </div>
            </div>
            <%if ((user == null) || (user != null && p.getId().getOwnerUserId() != user.getUserId())) {%>
            <div class="onerow">
                <div class="col12 last" >
                    <h1>Communication</h1> </div> </div>
            <div class="onerow">
                <div class="col6">
                    <h2>Contact Form</h2>
                    <%if (user != null) {%>
                    <form id="general-form" autocomplete="off" name="pform" method="post" action="sendmsg?sender_id=<%=user.getUserId()%>&amp;owner_id=<%=owner.getUserId()%>&amp;pid=<%=p.getId().getPropertyId()%>">
                        <p class="general">
                            <label for="sendermail">Email Sender <font color="red">*</font>
                            </label></p>
                        <input type="text" name="sendermail" id="sender"/>   
                        <p class="general">
                            <label for="msg">Message <font color="red">*</font>
                            </label></p>
                        <textarea name="msg" rows="5" cols="48" id="msg" >
                        </textarea>
                        <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>	 
                    </form><%} else {%>
                    <p> You must be logged in to be able to use this feature</p>
                    <%}%>
                </div>
                <div class="col6 last" >
                    <h2>Contact info</h2>
                    <p><b>Owner's name: </b><%=owner.getName() + " " + owner.getSurname()%>
                    <p><b>Email: </b><a href="emailto:<%=owner.getEmail()%>"><%=owner.getEmail()%></a></p>
                    <p><b>Phone: </b><%=(owner.getTelephone() != null) ? owner.getTelephone() : "-"%>
                    <p><b>Phone 2: </b><%=(owner.getTelephone2() != null) ? owner.getTelephone2() : "-"%>
                </div> 
            </div>
            <%} else if (user != null && user.getUserId() == owner.getUserId()) {
                List<Messages> msgs = PropertyIO.getmsgs(user.getUserId(), Integer.parseInt(pid));
            %>
            <div class="onerow">
                <div class="col12 last">
                    <h1>Received Messages</h1>
                </div></div>
            <div class="onerow">
                <div class="col12 last">
                    <%if (msgs.size() == 0) { %>
                    <p>There is no message about this property.</p>
                    <%} else {
                        int i = 1;
                        for (Messages m : msgs) {
                    %>
                    <a href="readmsg.jsp?uid=<%=uid%>&amp;mid=<%=m.getId().getMessageId()%>&amp;pid=<%=pid%>"><b>Message <%=i%></b></a><br/>
                    <%i++;
                            }
                        }%>
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