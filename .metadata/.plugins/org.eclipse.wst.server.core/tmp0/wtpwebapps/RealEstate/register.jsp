<%@page import="entities.User"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - New User Registration</title>	
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
                <div class="col12 last">
                    <h1>New User Registration</h1>
                    <form autocomplete="off" name="register-form" id="general-form" action="registration" onsubmit="return validateRegisterForm()" method="POST">
                        <p class="general"><label for="username">Username <font color="red">*</font>
                            </label></p>
                        <input id="username" type="text" name="username" placeholder="user"/>
                        <p class="general"><label for="password">Password <font color="red">*</font>
                            </label></p>
                        <input id="password" type="password" name="password"  />
                        <p class="general"><label for="password2">Confirm Password <font color="red">*</font>
                            </label></p>
                        <input id="password2" type="password" name="password2"  />
                        <p class="general"><label for="email">Email <font color="red">*</font>
                            </label></p>
                        <input id="email" type="email" name="email"  placeholder="Email"/>
                        <p class="general">
                            <label for="name">Name <font color="red">*</font>
                            </label></p>
                        <input id="name" type="text" name="name" placeholder="Name"/>
                        <p class="general">
                            <label for="middle_name">Middle name
                            </label></p>
                        <input id="middle_name" type="text" name="middle_name" placeholder="Middle name"/>
                        <p class="general">
                            <label for="surname">Last name <font color="red">*</font>
                            </label></p>
                        <input id="surname" type="text" name="surname" placeholder="Last name"/>
                        <fieldset>
                            <label for="lessor">Lessor
                            </label>
                            <input id="lessor" type="checkbox" value="1" name="lessor" />

                            <label for="seller">Seller
                            </label>
                            <input id="seller" type="checkbox" value="1" name="seller" />

                            <label for="lessee">Tenant
                            </label>
                            <input id="lessee" type="checkbox" value="1" name="lessee" />

                            <label for="buyer">Buyer
                            </label>
                            <input id="buyer" type="checkbox" value="1" name="buyer" />
                        </fieldset>
                        <p class="general">
                            <label for="telephone1" > Phone
                            </label> </p>
                        <input id="telephone1"  type="text" name="telephone1" placeholder="9876543210" />
                        <p class="general">
                            <label for="telephone_2" > Phone 2
                            </label> </p>
                        <input id="telephone_2"  type="text" name="telephone_2"  placeholder="9876543210" />
                        <p class="general">
                            <label for="fax" > FAX
                            </label> </p>
                        <input id="fax"  type="text" name="fax"  />
                        <p class="general">
                            <label for="address1" > Address 1 <font color="red">*</font>
                            </label> </p>
                        <input id="address1"  type="text" name="address1"  placeholder="Address 1"/>
                        <p class="general">
                            <label for="address2"> Address 2 
                            </label> </p>
                        <input id="address2" type="text" name="address2" />
                        <p class="general">
                            <label for="address3"> Address 3
                            </label></p>
                        <input id="address3" type="text" name="address3"  />
                        <p class="general">
                            <label for="city"> City <font color="red">*</font>
                            </label></p>
                        <input id="city" type="text" name="city" placeholder="City"/>
                        <p class="general">
                            <label for="region"> Area <font color="red">*</font>
                            </label></p> 
                        <input id="region" type="text" name="region" placeholder="Area"/>
                        <p class="general">
                            <label for="postalCode"> Postcodes <font color="red">*</font>
                            </label></p>
                        <input id="postalCode" type="text" name="postalCode" placeholder="411043"/>
                        <p class="general"><label for="country"> Country <font color="red">*</font>
                            </label></p>
                            <% String[] locales = Locale.getISOCountries(); %>
                        <select id="country" name="country">
                            <%
                                for (String locale : locales) {
                                    Locale _locale = new Locale("", locale);
                                    if (_locale.getDisplayCountry().equals("India")) {
                                        out.println("<option selected value=\"" + _locale.getDisplayCountry() + "\">" + _locale.getDisplayCountry() + "</option>");
                                    } else {
                                        out.println("<option value=\"" + _locale.getDisplayCountry() + "\">" + _locale.getDisplayCountry() + "</option>");
                                    }
                                }
                            %>
                        </select>
                        <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>	 
                    </form>
                    <p>(<font color="red">*</font> Compulsory field)</p>
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
