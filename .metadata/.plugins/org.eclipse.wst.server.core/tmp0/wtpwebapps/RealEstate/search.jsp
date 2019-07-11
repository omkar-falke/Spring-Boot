<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Locale"%>
<%@page import="service.PropertyIO"%>
<%@page import="entities.Property"%>
<%@page import="entities.Property"%>
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
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
        <script>
            function changeslider() {
                var sr = document.getElementById("Sale_Rent").value;
                if (sr === "Sale") {
                    $("#slider-range").slider({min: 100, max: 9999999, values: [100, 9999999]});
                } else if (sr === "Rent") {
                    $("#slider-range").slider({min: 20, max: 5000, values: [20, 5000]});
                }
                $("#amount").val("₹" + $("#slider-range").slider("values", 0) +
                        " - ₹" + $("#slider-range").slider("values", 1));
            }

            $(function () {
                $("#slider-range").slider({
                    range: true,
                    min: 20,
                    max: 9999999,
                    values: [20, 9999999],
                    step: 5,
                    slide: function (event, ui) {
                        $("#amount").val("₹" + ui.values[ 0 ] + " - ₹" + ui.values[ 1 ]);
                    }
                });
                $("#amount").val("₹" + $("#slider-range").slider("values", 0) +
                        " - ₹" + $("#slider-range").slider("values", 1));
            });

            $(function () {
                $("#slider-mrange").slider({
                    range: true,
                    min: 0,
                    max: 1000,
                    values: [0, 1000],
                    step: 5,
                    slide: function (event, ui) {
                        $("#mamount").val("₹" + ui.values[ 0 ] + " - ₹" + ui.values[ 1 ]);
                    }
                });
                $("#mamount").val("₹" + $("#slider-mrange").slider("values", 0) +
                        " - ₹" + $("#slider-mrange").slider("values", 1));
            });

            $(function () {
                $("#slider-brange").slider({
                    range: true,
                    min: 1950,
                    max: 2018,
                    values: [1950, 2018],
                    step: 5,
                    slide: function (event, ui) {
                        $("#bamount").val(ui.values[ 0 ] + " - " + ui.values[ 1 ]);
                    }
                });
                $("#bamount").val($("#slider-brange").slider("values", 0) +
                        " - " + $("#slider-brange").slider("values", 1));
            });

            $(function () {
                $("#slider-srange").slider({
                    range: true,
                    min: 10,
                    max: 1000,
                    values: [10, 1000],
                    step: 5,
                    slide: function (event, ui) {
                        $("#samount").val(ui.values[ 0 ] + " - " + ui.values[ 1 ]);
                    }
                });
                $("#samount").val($("#slider-srange").slider("values", 0) +
                        " - " + $("#slider-srange").slider("values", 1));
            });
        </script>
        <title>Find A Residence - Property Search</title>	
    </head>
    <body> 
        <div class="onepcssgrid-1200">
            <div class="onerow">
                <div class="col5">
                    <a href="index.jsp"><img src="images/far.png" alt="logo"/></a>
                </div>
                <div class="col7 last">
                    <%
                        User user = (User) session.getAttribute("user");
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
                                    <li><a href='myproperties.jsp'>My Properties;</a></li> <%}%>
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
                <div class="col12 last" >
                    <h1>Property Search</h1>
                </div> </div>
            <div class="onerow">
                <%List<Property> properties = PropertyIO.getallProperties();
                    Set<String> cities = new HashSet<String>();
                    for (Property p : properties) {
                        if (!cities.contains(p.getCity())) {
                            cities.add(p.getCity());
                        }
                    }
                %>
                <div class="col12 last">
                    <form id="general-form" autocomplete="off" name="pform" method="post" action="searchproperty">
                        <p class="general"><label for="city">Available Cities
                            </label></p>
                        <select name="city" id="city"> 
                            <option value="Any">Anyone</option>
                            <%for (String city : cities) {%>
                            <option value="<%=city%>"><%=city%></option>
                            <%}%> 
                        </select>  
                        <p class="general"></p>
                        <fieldset> 
                            <label for="Sale_Rent">Transaction type
                            </label>
                            <select name="Sale_Rent" id="Sale_Rent" onchange="changeslider()"> 
                                <option value="Sale">Sale</option>
                                <option value="Rent">Tenancy</option>                                
                            </select>
                            <label for="Apartment_House">Type of Property 
                            </label>
                            <select name="Apartment_House" id="Apartment_House">
                                <option value="Both">All</option>
                                <option value="Apartment">Apartment</option>
                                <option value="House">Detached house</option>
                            </select>  
                        </fieldset>
                        <p class="general"></p>

                        <fieldset>
                            <label for="heatingSystem">Heating system
                            </label>
                            <input type="checkbox" value="1" name="heatingSystem" id="heatingSystem"/>

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
                        <p class="general"><label for="amount">Price Range / Rent
                            </label></p>
                        <input type="text" id="amount" name="amount" readonly style="text-align: center; border:0;">
                        <div id="slider-range"></div>
                        <p class="general"><label for="amountw">Gravity
                            </label>
                            <select name="amountw" id="amountw"> 
                                <option value="5">1</option>
                                <option value="4">2</option>                                
                                <option value="3">3</option>                                
                                <option value="2">4</option>                                
                                <option value="1">5</option>                                
                            </select>
                        <p class="general"></p>

                        <p class="general"><label for="mamount">Range of Maintenance Expenses
                            </label></p>
                        <input type="text" id="mamount" name="mamount" readonly style="text-align: center; border:0;">
                        <div id="slider-mrange"></div>
                        <p class="general"><label for="mamountw">Gravity
                            </label>
                            <select name="mamountw" id="amountw"> 
                                <option value="5">1</option>
                                <option value="4">2</option>                                
                                <option value="3">3</option>                                
                                <option value="2">4</option>                                
                                <option value="1">5</option>                                
                            </select>
                        <p class="general"></p>

                        <p class="general"><label for="bamount">Manufacturing date
                            </label></p>
                        <input type="text" id="bamount" name="bamount" readonly style="text-align: center; border:0;">
                        <div id="slider-brange"></div>
                        <p class="general"><label for="bamountw">Gravity
                            </label>
                            <select name="bamountw" id="bamountw"> 
                                <option value="1">1</option>
                                <option value="2">2</option>                                
                                <option value="3">3</option>                                
                                <option value="4">4</option>                                
                                <option value="5">5</option>                                
                            </select>
                        <p class="general"></p>

                        <p class="general"><label for="samount">Square meters
                            </label></p>
                        <input type="text" id="samount" name="samount" readonly style="text-align: center; border:0;">
                        <div id="slider-srange"></div>
                        <p class="general"><label for="samountw">Gravity
                            </label>
                            <select name="samountw" id="samountw"> 
                                <option value="1">1</option>
                                <option value="2">2</option>                                
                                <option value="3">3</option>                                
                                <option value="4">4</option>                                
                                <option value="5">5</option>                                
                            </select>

                        <p><input class="buttom" name="submit" id="submit" value="search" type="submit"> </p>	 
                    </form>
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