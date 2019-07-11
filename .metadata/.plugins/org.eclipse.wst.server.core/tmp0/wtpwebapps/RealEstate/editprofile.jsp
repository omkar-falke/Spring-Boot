<%@page import="entities.User"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - Edit Profile</title>	
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
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <h1>Edit Profile</h1>   
                    <form id="general-form" name="form" method="post" action="editprofile"  onsubmit="return validateEditProfileForm()">
                        <p class="general">
                            <label for="username">Username 
                            </label></p>
                        <output id="username" name="username" ><%=user.getUsername()%></output>
                        <p class="general">
                        </p>
                        <p class="general">
                            <label>Email <font color="red">*</font>
                            </label></p>
                        <input type="text" name="email" value="<%=user.getEmail()%>"  />
                        <p class="general">
                            <label>Name <font color="red">*</font>
                            </label></p>
                        <input type="text" name="name" value="<%=user.getName()%>"  />
                        <p class="general">
                            <label>Middle name
                            </label></p>
                        <input type="text" name="middle_name" value="<%=user.getMiddleName()%>"  />
                        <p class="general">
                            <label>Last name <font color="red">*</font>
                            </label></p>
                        <input type="text" name="surname" value="<%=user.getSurname()%>" />
                        <fieldset>
                            <label>Lessor
                            </label>
                            <% if (user.isLessor()) {%>
                            <input type="checkbox" name="lessor" value="1" checked  /> <% } else { %>
                            <input type="checkbox" name="lessor" value="1"   /> <% } %>

                            <label>Seller
                            </label>
                            <% if (user.isSeller()) {%>
                            <input type="checkbox"  name="seller" value="1"  checked/> <%} else { %>
                            <input type="checkbox"   name="seller" value="1" /> <% }%>

                            <label>Tenant
                            </label>
                            <% if (user.isLessee()) {%>
                            <input type="checkbox" name="lessee" value="1" checked /> <% } else { %>
                            <input type="checkbox" name="lessee" value="1" /> <% } %>

                            <label>Buyer
                            </label>
                            <% if (user.isBuyer()) {%>
                            <input type="checkbox"  name="buyer" value="1" checked /> <% } else { %>
                            <input type="checkbox"  name="buyer" value="1"  /> <% }%>
                        </fieldset>
                        <p class="general">
                            <label>Address 1 <font color="red">*</font>
                            </label></p>
                        <input type="text" name="address1" value="<%=user.getAddress1()%>"/>
                        <p class="general">
                            <label>Address 2
                            </label></p>
                        <input type="text" name="address2" value="<%=user.getAddress2()%>"/>
                        <p class="general">
                            <label>Address 3
                            </label></p>
                        <input type="text" name="address3"  value="<%=user.getAddress3()%>"/>
                        <p class="general">
                            <label>City <font color="red">*</font>
                            </label></p>
                        <input type="text" name="city" value="<%=user.getCity()%>"/>
                        <p class="general">
                            <label>Area <font color="red">*</font>
                            </label></p>
                        <input type="text" name="region" value="<%=user.getRegion()%>"/>
                        <p class="general">
                            <label>Postcodes <font color="red">*</font>
                            </label></p>
                        <input type="text" name="postalCode"  value="<%=user.getPostalCode()%>"/>
                        <p class="general">
                            <label>Country <font color="red">*</font>
                            </label></p>
                            <% String[] locales = Locale.getISOCountries(); %>
                        <select name="country">
                            <%
                                for (String locale : locales) {
                                    Locale _locale = new Locale("", locale);
                                    if (user.getCountry().equals(_locale.getDisplayCountry())) {
                                        out.println("<option value=\"" + user.getCountry() + "\" selected=\"selected\">" + user.getCountry() + "</option>");
                                    } else {
                                        out.println("<option value=\"" + _locale.getDisplayCountry() + "\">" + _locale.getDisplayCountry() + "</option>");
                                    }
                                }
                            %>
                        </select>
                        <p><input class="buttom" name="submit" id="submit" value="Submit" type="submit"> </p>	 
                    </form>
                    <p>(<font color="red">*</font> Compulsory field)</p>
                    <p><a href="changepassword.jsp"> Change Password</a></p>
                    <p><a href="setprofilepicture.jsp"> Change User Image</a></p>
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
