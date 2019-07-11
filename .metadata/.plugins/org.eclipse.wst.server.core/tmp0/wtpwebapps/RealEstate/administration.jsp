<%@page import="service.AlgorithmIO"%>
<%@page import="entities.User"%>
<%@page import="entities.Admin"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Files"%>
<%@page import="javax.script.Invocable"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="javax.script.ScriptEngine"%>
<%@page import="javax.script.ScriptEngineManager"%>
<%@page import="service.UserIO"%>
<%@page import="java.util.List"%>
<%@page import="misc.IP_Functions"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/admin_style.css" rel="stylesheet" type="text/css"/>
        <script src="javascript/admin_scripts.js" type="text/javascript"></script>
        <title>Find A Residence - Manage Page</title>	
    </head>
    <body> 
        <%
            Admin admin = (Admin) session.getAttribute("admin");
            if (admin == null) {
        %>	
        <div class="onepcssgrid-1200">
            <div class="onerow">
                <div class="col12 last">
                    <h1>Administrator login</h1>
                    <form id="login-form" action="administration">
                        <p><label for="username">Administrator name</label></p>
                        <input type="text" name="username" id="username"/>
                        <p><label for="password">Password</label></p>
                        <input type="password" name="password" id="password"/>
                        <p><input class="buttom" name="submit" id="submit" value="Login" type="submit"> </p>	 
                    </form>
                </div>
            </div>
        </div>
        <%} else {%>
        <div class="onepcssgrid-1200">
            <div class="onerow">  
                <div class="col12 last">
                    <a href="logout.jsp">LOGOUT</a>
                </div>
            </div>
        </div>

        <div class="onepcssgrid-1200">
            <%List<User> users = UserIO.getUsers();%>
            <div class="onerow">      
                <div class="col12 last">
                    <div class="info_table" >
                        <table>
                            <tr>
                                <td>
                                    Active Users (Exit)
                                </td>
                            </tr>
                            <%for (User user : users) {
                                    if (user.isVerified()) {
                                        out.print("<tr><td><a href=\"#\" onclick=\"extend('user_info_" + user.getUserId() + "')\">" + user.getUsername() + "</a>"
                                                + "<div id=\"user_info_" + user.getUserId() + "\" style=\"display:none;\">"
                                                + "<table>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>User Details</td>"
                                                + "<td>  <a href=\"unvalidate?username=" + user.getUsername() + "\"><font color=\"green\">REMOVE</font></a>  </td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>ID</td>"
                                                + "<td>" + user.getUserId() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Username</td>"
                                                + "<td>" + user.getUsername() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Email</td>"
                                                + "<td>" + user.getEmail() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Name</td>"
                                                + "<td>" + user.getName() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Middle name</td>"
                                                + "<td>" + ((user.getMiddleName() != null) ? user.getMiddleName() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Last name</td>"
                                                + "<td>" + user.getSurname() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Phone</td>"
                                                + "<td>" + ((user.getTelephone() != null) ? user.getTelephone() : "") + "</td>"
                                                + "</tr>" + "<tr>"
                                                + "<td>Phone 2</td>"
                                                + "<td>" + ((user.getTelephone2() != null) ? user.getTelephone2() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>FAX</td>"
                                                + "<td>" + ((user.getFax() != null) ? user.getFax() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Lessor</td>"
                                                + "<td>" + (user.isLessor() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Seller</td>"
                                                + "<td>" + (user.isSeller() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Tenant</td>"
                                                + "<td>" + (user.isLessee() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Buyer</td>"
                                                + "<td>" + (user.isBuyer() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Address 1</td>"
                                                + "<td>" + user.getAddress1() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Address 2</td>"
                                                + "<td>" + ((user.getAddress2() != null) ? user.getAddress2() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Address 3</td>"
                                                + "<td>" + ((user.getAddress3() != null) ? user.getAddress3() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>City</td>"
                                                + "<td>" + user.getCity() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Postcodes</td>"
                                                + "<td>" + user.getPostalCode() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Region</td>"
                                                + "<td>" + user.getRegion() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Country</td>"
                                                + "<td>" + user.getCountry() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Registration date</td>"
                                                + "<td>" + user.getJoinDate() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>IP Registration</td>"
                                                + "<td>" + IP_Functions.unpack(user.getRegisterIp()) + "</td>"
                                                + "</tr>"
                                                + "</table>"
                                                + "</div>"
                                                + "</td></tr>"
                                        );
                                    }
                                }%> 
                        </table>
                    </div>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <div class="info_table" >
                        <table >
                            <tr>
                                <td>
                                    NEW / NON-ACTIVE USERS
                                </td>
                            </tr>
                            <%for (User user : users) {
                                    if (!user.isVerified()) {
                                        out.print("<tr><td><a href=\"#\" onclick=\"extend('user_info_" + user.getUserId() + "')\">" + user.getUsername() + "</a>"
                                                + "<div id=\"user_info_" + user.getUserId() + "\" style=\"display:none;\">"
                                                + "<table>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>User Details</td>"
                                                + "<td>  <a href=\"validate?username=" + user.getUsername() + "\"><font color=\"red\"> ADD as a ACTIVE USER</font></a>  </td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>ID</td>"
                                                + "<td>" + user.getUserId() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Username</td>"
                                                + "<td>" + user.getUsername() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Email</td>"
                                                + "<td>" + user.getEmail() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Name</td>"
                                                + "<td>" + user.getName() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Middle name</td>"
                                                + "<td>" + ((user.getMiddleName() != null) ? user.getMiddleName() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Last name</td>"
                                                + "<td>" + user.getSurname() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Phone</td>"
                                                + "<td>" + ((user.getTelephone() != null) ? user.getTelephone() : "") + "</td>"
                                                + "</tr>" + "<tr>"
                                                + "<td>Phone 2</td>"
                                                + "<td>" + ((user.getTelephone2() != null) ? user.getTelephone2() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>FAX</td>"
                                                + "<td>" + ((user.getFax() != null) ? user.getFax() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Lessor</td>"
                                                + "<td>" + (user.isLessor() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Seller</td>"
                                                + "<td>" + (user.isSeller() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Tenant</td>"
                                                + "<td>" + (user.isLessee() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Buyer</td>"
                                                + "<td>" + (user.isBuyer() ? "ΝΑΙ" : "ΟΧΙ") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Address 1</td>"
                                                + "<td>" + user.getAddress1() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Address 2</td>"
                                                + "<td>" + ((user.getAddress2() != null) ? user.getAddress2() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Address 3</td>"
                                                + "<td>" + ((user.getAddress3() != null) ? user.getAddress3() : "") + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>City</td>"
                                                + "<td>" + user.getCity() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Postcodes</td>"
                                                + "<td>" + user.getPostalCode() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Region</td>"
                                                + "<td>" + user.getRegion() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Country</td>"
                                                + "<td>" + user.getCountry() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>Registration date</td>"
                                                + "<td>" + user.getJoinDate() + "</td>"
                                                + "</tr>"
                                                + "<tr>"
                                                + "<td>IP Registration</td>"
                                                + "<td>" + IP_Functions.unpack(user.getRegisterIp()) + "</td>"
                                                + "</tr>"
                                                + "</table>"
                                                + "</div>"
                                                + "</td></tr>"
                                        );
                                    }
                                }%> 
                        </table>
                    </div>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <div class="info_table" >
                        <table >
                            <tr>
                                <td>
                                    ALGORITHM OF CLASSIFICATION
                                </td>
                            </tr>
                            <tr>
                                <%if (AlgorithmIO.runningSaw()) {%>
                                <td><font color="green">SAW</font></td>
                                    <%} else {%>
                                <td><a href="changealgorithm?algorithm=saw"><font color="red">SAW</font></a></td>
                                        <%}%>
                            </tr>
                            <tr>
                                <%if (AlgorithmIO.runningTopsis()) {%>
                                <td><font color="green">TOPSIS</font></td>
                                    <%} else {%>
                                <td><a href="changealgorithm?algorithm=topsis"><font color="red">TOPSIS</font></a></td>
                                        <%}%>             
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
    </body>
</html>