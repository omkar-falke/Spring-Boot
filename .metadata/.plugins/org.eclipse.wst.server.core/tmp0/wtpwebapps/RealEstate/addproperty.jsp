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
        <title>Find A Residence - Add New Property</title>	
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            if (user == null || (!user.isLessor() && !user.isSeller())) {
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
                                    <li><a href='logout.jsp'>Exit</a></li>
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
                    <h1>Add New Property</h1> </div> </div>
            <div class="onerow">
                <div class="col6">
                    <form id="general-form" autocomplete="off" name="pform" method="post" action="addproperty" onsubmit="return validatePropertyForm()">
                        <fieldset> 
                            <label for="forSale_forRent">Transaction type <font color="red">*</font>
                            </label>
                            <select name="forSale_forRent" id="forSale_forRent">  
                                <%if (user.isSeller() && user.isLessor()) {%>
                                <option value="forSale">Sale</option>
                                <option value="forRent">Tenancy</option>
                                <%} else if (user.isSeller()) {%>
                                <option value="forSale">Sale</option>
                                <%} else {%>
                                <option value="forRent">Tenancy</option>
                                <%}%>
                            </select>
                            <label for="isApartment_isHouse">Type of Property <font color="red">*</font>
                            </label>
                            <select name="isApartment_isHouse" id="isApartment_isHouse">           
                                <option value="isApartment">Apartment</option>
                                <option value="isHouse">Detached house </option>
                            </select>               
                        </fieldset>
                        <p class="general"></p>
                        <p class="general"></p>
                        <fieldset>
                            <label for="buildDate">Manufacturing date <font color="red">*</font>
                            </label>
                            <select name="buildDate" id="buildDate">
                                <%
                                    for (int i = 1950; i <= 2017; i++) {
                                        out.println("<option  value=" + i + ">" + i + "</option>");
                                    }
                                %>
                                <option value ="2018" selected>2018</option>
                            </select>

                            <label for="renovationDate">Renovation Date
                            </label>
                            <select name="renovationDate" id="renovationDate">
                                <option value = "" selected></option>
                                <%
                                    for (int i = 1950; i <= 2018; i++) {
                                        out.println("<option  value=" + i + ">" + i + "</option>");
                                    }
                                %>
                            </select>                
                        </fieldset>
                        <br/>
                        <p class="general">
                            <label for="price">Price <font color="red">*</font>
                            </label></p>
                        <input type="text" name="price" id="price" placeholder="125000"/>      
                        <br/>    
                        <p class="general">
                            <label for="maintenanceCharges">Maintenance Fees / Month <font color="red">*</font>
                            </label></p>
                        <input type="text" name="maintenanceCharges" id="maintenanceCharges" placeholder="120" /> 
                        <p class="general">
                            <label for="sqMeters">Square meters <font color="red">*</font>
                            </label></p>
                        <input type="text" name="sqMeters" id="sqMeters" placeholder="120"/> 
                        <p class="general">
                            <label for="roomsNo">Number of rooms <font color="red">*</font>
                            </label></p>
                        <input type="text" name="roomsNo" id="roomsNo" placeholder="3"/>   
                        <p class="general">
                            <label for="floor">Floor / Floors <font color="red">*</font>
                            </label></p>
                        <input type="text" name="floor" id="floor" placeholder="3"/>             
                        <p class="general">
                            <label for="heatingSystem">Heating system
                            </label>
                            <select name="heatingSystem" id="heatingSystem">
                                <option value="none"></option>
                                <option value="central_diesel">Central Heating Oil</option>
                                <option value="atomic_diesel">Individual Heating Oil</option>
                                <option value="central_gas">Central Heating of Natural Gas</option>
                                <option value="atomic_gas">Individual Natural Gas Heating</option>
                                <option value="pelet">Heating with Pelet</option>
                            </select>
                        <fieldset>
                            <label for="airConditioner">Air conditioning
                            </label>
                            <input type="checkbox" value="1" name="airConditioner" id="airConditioner"/>

                            <label for="parking">Parking lot
                            </label>
                            <input type="checkbox" value="1" name="parking" id="parking"/>
                            <label for="elevator">Elevator
                            </label>
                            <input type="checkbox" value="1" name="elevator" id="elevator"/>
                        </fieldset>
                        <p class="general">
                            <label for="description">Description
                            </label></p>
                        <textarea name="description" rows="5" cols="48" id="description" >
                        </textarea>
                        <p class="general">
                            <label for="address1">Address 1 <font color="red">*</font>
                            </label></p>
                        <input type="text" name="address1" id="address1"  placeholder="Address 1" onchange="getlatlng()" />                
                        <p class="general">
                            <label for="address2">Address 2
                            </label></p>
                        <input type="text" name="address2" id="address2"  onchange="getlatlng()"/>                
                        <p class="general">
                            <label for="address3">Address 3
                            </label></p>
                        <input type="text" name="address3" id="address3"  onchange="getlatlng()"/>             
                        <p class="general">
                            <label for="city">City <font color="red">*</font>
                            </label></p>
                        <input type="text" name="city" placeholder="City" id="city"  onchange="getlatlng()"/>            
                        <p class="general">
                            <label for="region">Area <font color="red">*</font>
                            </label></p>
                        <input type="text" name="region" placeholder="Area" id="region"  onchange="getlatlng()"/>     
                        <p class="general">
                            <label for="postalCode">Postcodes <font color="red">*</font>
                            </label></p>
                        <input type="text" name="postalCode" placeholder="411043" id="postalCode" onchange="getlatlng()"/>    
                        <p class="general">
                            <label for="country">Country <font color="red">*</font>
                            </label></p>
                            <% String[] locales = Locale.getISOCountries();%>
                        <select name="country" id="country"  onchange="getlatlng()">
                            <%
                                for (String locale : locales) {
                                    Locale _locale = new Locale("", locale);
                                    if (_locale.getDisplayCountry().equals("India")) {
                                        out.println("<option selected value=" + _locale.getDisplayCountry() + ">" + _locale.getDisplayCountry() + "</option>");
                                    } else {
                                        out.println("<option value=" + _locale.getDisplayCountry() + ">" + _locale.getDisplayCountry() + "</option>");
                                    }
                                }
                            %>
                        </select>
                        <input hidden type="text" id="lat" name="lat" value="" />
                        <input hidden type="text" id="lng" name="lng" value="" />
                        <p class="general"></p>
                        <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>	 
                    </form>
                    <p>(<font color="red">*</font> Compulsory field)</p>
                </div>
                <div class="col6 last">
                    <div id="map-canvas" style="height:1000px"></div>
                </div>
            </div>
            <div class="onerow">
                <div class="col1">             
                    <a href="index.jsp"><img src="images/farfooter.png" alt="logo"/></a>
                </div>
                <div class="col3"><p><b>Find A Residence &copy;</b><br>Ysupport of Residential / Residential Decisions</p></div>
                <div class="col4">             
                    <p><b>Powered by:</b><br/>Infoideal technologies - 9812345648<br/>Office- 9812345648</p>
                </div>
                <div class="col4 last">             
                    <p><a href="mailto:akashwaghmare54@gmail.com?Subject=Find A Residence" target="_top">Contact us</a></p>
                </div>
            </div> 
        </div>
        <script>
            var map;
            var marker;
            function initialize() {
                var mapOptions = {
                    zoom: 7,
                    center: new google.maps.LatLng(38.2749497, 23.8102716)
                };
                map = new google.maps.Map(document.getElementById('map-canvas'),
                        mapOptions);
                marker = new google.maps.Marker({title: "Property"});
                marker.setPosition(new google.maps.LatLng(20.5937, 78.9629));
                marker.setMap(map);
            }
            google.maps.event.addDomListener(window, 'load', initialize);
            function getlatlng() {
                geocoder = new google.maps.Geocoder();
                var address1 = document.getElementById("address1").value;
                var address2 = document.getElementById("address2").value;
                var address3 = document.getElementById("address3").value;
                var city = document.getElementById("city").value;
                var region = document.getElementById("region").value;
                var postalCode = document.getElementById("postalCode").value;
                var country = document.getElementById("country").value;
                var address = address1 + " " + address2 + " " + address3 + " " + city + " " + region + " " + postalCode + " " + country;
                geocoder.geocode({'address': address}, function (results, status) {
                    if (status === google.maps.GeocoderStatus.OK) {
                        var latitude = results[0].geometry.location.lat();
                        var longitude = results[0].geometry.location.lng();
                        document.getElementById('lat').value = latitude.toString();
                        document.getElementById('lng').value = longitude.toString();
                    }
                });
                if (address1 !== "" && city !== "" && region !== "" && postalCode !== "") {
                    var panPoint = new google.maps.LatLng(document.getElementById("lat").value, document.getElementById("lng").value);
                    marker.setPosition(panPoint);
                    map.panTo(panPoint);
                    map.setZoom(16);
                }
            }
        </script>
    </body>
</html>