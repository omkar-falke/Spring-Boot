/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.41
 * Generated at: 2019-06-30 14:17:58 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Locale;
import service.PropertyIO;
import entities.Property;
import entities.Property;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.script.Invocable;
import java.nio.charset.StandardCharsets;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import service.UserIO;
import java.util.List;
import entities.User;
import misc.IP_Functions;
import java.util.Date;

public final class search_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("service.PropertyIO");
    _jspx_imports_classes.add("java.nio.file.Files");
    _jspx_imports_classes.add("java.util.Date");
    _jspx_imports_classes.add("java.util.Set");
    _jspx_imports_classes.add("javax.script.ScriptEngineManager");
    _jspx_imports_classes.add("java.nio.charset.StandardCharsets");
    _jspx_imports_classes.add("java.util.ArrayList");
    _jspx_imports_classes.add("java.util.HashSet");
    _jspx_imports_classes.add("entities.User");
    _jspx_imports_classes.add("java.util.List");
    _jspx_imports_classes.add("javax.script.Invocable");
    _jspx_imports_classes.add("java.nio.file.Paths");
    _jspx_imports_classes.add("java.util.Locale");
    _jspx_imports_classes.add("javax.script.ScriptEngine");
    _jspx_imports_classes.add("entities.Property");
    _jspx_imports_classes.add("service.UserIO");
    _jspx_imports_classes.add("misc.IP_Functions");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
return;
}

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/>\n");
      out.write("        <script src=\"javascript/scripts.js\" type=\"text/javascript\"></script>\n");
      out.write("        <link rel=\"stylesheet\" href=\"//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css\">\n");
      out.write("        <script src=\"//code.jquery.com/jquery-1.10.2.js\"></script>\n");
      out.write("        <script src=\"//code.jquery.com/ui/1.11.1/jquery-ui.js\"></script>\n");
      out.write("        <script>\n");
      out.write("            function changeslider() {\n");
      out.write("                var sr = document.getElementById(\"Sale_Rent\").value;\n");
      out.write("                if (sr === \"Sale\") {\n");
      out.write("                    $(\"#slider-range\").slider({min: 100, max: 9999999, values: [100, 9999999]});\n");
      out.write("                } else if (sr === \"Rent\") {\n");
      out.write("                    $(\"#slider-range\").slider({min: 20, max: 5000, values: [20, 5000]});\n");
      out.write("                }\n");
      out.write("                $(\"#amount\").val(\"₹\" + $(\"#slider-range\").slider(\"values\", 0) +\n");
      out.write("                        \" - ₹\" + $(\"#slider-range\").slider(\"values\", 1));\n");
      out.write("            }\n");
      out.write("\n");
      out.write("            $(function () {\n");
      out.write("                $(\"#slider-range\").slider({\n");
      out.write("                    range: true,\n");
      out.write("                    min: 20,\n");
      out.write("                    max: 9999999,\n");
      out.write("                    values: [20, 9999999],\n");
      out.write("                    step: 5,\n");
      out.write("                    slide: function (event, ui) {\n");
      out.write("                        $(\"#amount\").val(\"₹\" + ui.values[ 0 ] + \" - ₹\" + ui.values[ 1 ]);\n");
      out.write("                    }\n");
      out.write("                });\n");
      out.write("                $(\"#amount\").val(\"₹\" + $(\"#slider-range\").slider(\"values\", 0) +\n");
      out.write("                        \" - ₹\" + $(\"#slider-range\").slider(\"values\", 1));\n");
      out.write("            });\n");
      out.write("\n");
      out.write("            $(function () {\n");
      out.write("                $(\"#slider-mrange\").slider({\n");
      out.write("                    range: true,\n");
      out.write("                    min: 0,\n");
      out.write("                    max: 1000,\n");
      out.write("                    values: [0, 1000],\n");
      out.write("                    step: 5,\n");
      out.write("                    slide: function (event, ui) {\n");
      out.write("                        $(\"#mamount\").val(\"₹\" + ui.values[ 0 ] + \" - ₹\" + ui.values[ 1 ]);\n");
      out.write("                    }\n");
      out.write("                });\n");
      out.write("                $(\"#mamount\").val(\"₹\" + $(\"#slider-mrange\").slider(\"values\", 0) +\n");
      out.write("                        \" - ₹\" + $(\"#slider-mrange\").slider(\"values\", 1));\n");
      out.write("            });\n");
      out.write("\n");
      out.write("            $(function () {\n");
      out.write("                $(\"#slider-brange\").slider({\n");
      out.write("                    range: true,\n");
      out.write("                    min: 1950,\n");
      out.write("                    max: 2018,\n");
      out.write("                    values: [1950, 2018],\n");
      out.write("                    step: 5,\n");
      out.write("                    slide: function (event, ui) {\n");
      out.write("                        $(\"#bamount\").val(ui.values[ 0 ] + \" - \" + ui.values[ 1 ]);\n");
      out.write("                    }\n");
      out.write("                });\n");
      out.write("                $(\"#bamount\").val($(\"#slider-brange\").slider(\"values\", 0) +\n");
      out.write("                        \" - \" + $(\"#slider-brange\").slider(\"values\", 1));\n");
      out.write("            });\n");
      out.write("\n");
      out.write("            $(function () {\n");
      out.write("                $(\"#slider-srange\").slider({\n");
      out.write("                    range: true,\n");
      out.write("                    min: 10,\n");
      out.write("                    max: 1000,\n");
      out.write("                    values: [10, 1000],\n");
      out.write("                    step: 5,\n");
      out.write("                    slide: function (event, ui) {\n");
      out.write("                        $(\"#samount\").val(ui.values[ 0 ] + \" - \" + ui.values[ 1 ]);\n");
      out.write("                    }\n");
      out.write("                });\n");
      out.write("                $(\"#samount\").val($(\"#slider-srange\").slider(\"values\", 0) +\n");
      out.write("                        \" - \" + $(\"#slider-srange\").slider(\"values\", 1));\n");
      out.write("            });\n");
      out.write("        </script>\n");
      out.write("        <title>Find A Residence - Property Search</title>\t\n");
      out.write("    </head>\n");
      out.write("    <body> \n");
      out.write("        <div class=\"onepcssgrid-1200\">\n");
      out.write("            <div class=\"onerow\">\n");
      out.write("                <div class=\"col5\">\n");
      out.write("                    <a href=\"index.jsp\"><img src=\"images/far.png\" alt=\"logo\"/></a>\n");
      out.write("                </div>\n");
      out.write("                <div class=\"col7 last\">\n");
      out.write("                    ");

                        User user = (User) session.getAttribute("user");
                        if (user == null) {
                    
      out.write("\n");
      out.write("                    <div id='cssmenu'>\n");
      out.write("                        <ul>\n");
      out.write("                            <li><a href='register.jsp'>Sign up</a></li>\n");
      out.write("                            <li onclick=\"popup('login')\"><a href='#'>Login</a></li>\n");
      out.write("                            <li><a href='search.jsp'>Property Search</a></li>\n");
      out.write("                        </ul>\n");
      out.write("                    </div>\n");
      out.write("                    ");
} else {
      out.write("\n");
      out.write("                    <div id='cssmenu'>\n");
      out.write("                        <ul>\n");
      out.write("                            <li><a href='#'>");
      out.print(user.getName());
      out.write("</a>\n");
      out.write("                                <ul>\n");
      out.write("                                    ");
if (user.isLessor() || user.isSeller()) {
      out.write("\n");
      out.write("                                    <li><a href='myproperties.jsp'>My Properties;</a></li> ");
}
      out.write("\n");
      out.write("                                    <li><a href='editprofile.jsp'>Edit Profile</a></li>\n");
      out.write("                                    <li><a href='logout.jsp'>Logout</a></li>\n");
      out.write("                                </ul></li>\n");
      out.write("                                ");
if (user.isLessor() || user.isSeller()) {
      out.write("\n");
      out.write("                            <li><a href='addproperty.jsp'>Property Registration</a></li>\n");
      out.write("                                ");
}
      out.write("\n");
      out.write("                            <li><a href='search.jsp'>Property Search</a></li>\n");
      out.write("                        </ul>\n");
      out.write("                    </div>\n");
      out.write("                    ");
}
      out.write("  \n");
      out.write("                </div>\n");
      out.write("                <div id=\"blanket\" style=\"display:none\" onclick=\"popup('login')\"></div>\n");
      out.write("                <div id=\"login\" style=\"display:none;\">\n");
      out.write("                    <button type=\"button\" onclick=\"popup('login')\">x</button>\n");
      out.write("                    <form id=\"login-form\" action=\"login\">\n");
      out.write("                        <input type=\"text\" placeholder=\"Username\" name=\"username\"/>\n");
      out.write("                        <input type=\"password\" placeholder=\"Password\" name=\"password\" />\n");
      out.write("                        <input type=\"submit\" value=\"Login\" />\n");
      out.write("                    </form>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"onerow\">\n");
      out.write("                <div class=\"col12 last\" >\n");
      out.write("                    <h1>Property Search</h1>\n");
      out.write("                </div> </div>\n");
      out.write("            <div class=\"onerow\">\n");
      out.write("                ");
List<Property> properties = PropertyIO.getallProperties();
                    Set<String> cities = new HashSet<String>();
                    for (Property p : properties) {
                        if (!cities.contains(p.getCity())) {
                            cities.add(p.getCity());
                        }
                    }
                
      out.write("\n");
      out.write("                <div class=\"col12 last\">\n");
      out.write("                    <form id=\"general-form\" autocomplete=\"off\" name=\"pform\" method=\"post\" action=\"searchproperty\">\n");
      out.write("                        <p class=\"general\"><label for=\"city\">Available Cities\n");
      out.write("                            </label></p>\n");
      out.write("                        <select name=\"city\" id=\"city\"> \n");
      out.write("                            <option value=\"Any\">Anyone</option>\n");
      out.write("                            ");
for (String city : cities) {
      out.write("\n");
      out.write("                            <option value=\"");
      out.print(city);
      out.write('"');
      out.write('>');
      out.print(city);
      out.write("</option>\n");
      out.write("                            ");
}
      out.write(" \n");
      out.write("                        </select>  \n");
      out.write("                        <p class=\"general\"></p>\n");
      out.write("                        <fieldset> \n");
      out.write("                            <label for=\"Sale_Rent\">Transaction type\n");
      out.write("                            </label>\n");
      out.write("                            <select name=\"Sale_Rent\" id=\"Sale_Rent\" onchange=\"changeslider()\"> \n");
      out.write("                                <option value=\"Sale\">Sale</option>\n");
      out.write("                                <option value=\"Rent\">Tenancy</option>                                \n");
      out.write("                            </select>\n");
      out.write("                            <label for=\"Apartment_House\">Type of Property \n");
      out.write("                            </label>\n");
      out.write("                            <select name=\"Apartment_House\" id=\"Apartment_House\">\n");
      out.write("                                <option value=\"Both\">All</option>\n");
      out.write("                                <option value=\"Apartment\">Apartment</option>\n");
      out.write("                                <option value=\"House\">Detached house</option>\n");
      out.write("                            </select>  \n");
      out.write("                        </fieldset>\n");
      out.write("                        <p class=\"general\"></p>\n");
      out.write("\n");
      out.write("                        <fieldset>\n");
      out.write("                            <label for=\"heatingSystem\">Heating system\n");
      out.write("                            </label>\n");
      out.write("                            <input type=\"checkbox\" value=\"1\" name=\"heatingSystem\" id=\"heatingSystem\"/>\n");
      out.write("\n");
      out.write("                            <label for=\"airConditioner\">Air conditioning\n");
      out.write("                            </label>\n");
      out.write("                            <input type=\"checkbox\" value=\"1\" name=\"airConditioner\" id=\"airConditioner\"/>\n");
      out.write("                            <label for=\"parking\">Parking lot\n");
      out.write("                            </label>\n");
      out.write("                            <input type=\"checkbox\" value=\"1\" name=\"parking\" id=\"parking\"/>\n");
      out.write("                            <label for=\"elevator\">Elevator\n");
      out.write("                            </label>\n");
      out.write("                            <input type=\"checkbox\" value=\"1\" name=\"elevator\" id=\"elevator\"/>\n");
      out.write("                        </fieldset>\n");
      out.write("                        <p class=\"general\"><label for=\"amount\">Price Range / Rent\n");
      out.write("                            </label></p>\n");
      out.write("                        <input type=\"text\" id=\"amount\" name=\"amount\" readonly style=\"text-align: center; border:0;\">\n");
      out.write("                        <div id=\"slider-range\"></div>\n");
      out.write("                        <p class=\"general\"><label for=\"amountw\">Gravity\n");
      out.write("                            </label>\n");
      out.write("                            <select name=\"amountw\" id=\"amountw\"> \n");
      out.write("                                <option value=\"5\">1</option>\n");
      out.write("                                <option value=\"4\">2</option>                                \n");
      out.write("                                <option value=\"3\">3</option>                                \n");
      out.write("                                <option value=\"2\">4</option>                                \n");
      out.write("                                <option value=\"1\">5</option>                                \n");
      out.write("                            </select>\n");
      out.write("                        <p class=\"general\"></p>\n");
      out.write("\n");
      out.write("                        <p class=\"general\"><label for=\"mamount\">Range of Maintenance Expenses\n");
      out.write("                            </label></p>\n");
      out.write("                        <input type=\"text\" id=\"mamount\" name=\"mamount\" readonly style=\"text-align: center; border:0;\">\n");
      out.write("                        <div id=\"slider-mrange\"></div>\n");
      out.write("                        <p class=\"general\"><label for=\"mamountw\">Gravity\n");
      out.write("                            </label>\n");
      out.write("                            <select name=\"mamountw\" id=\"amountw\"> \n");
      out.write("                                <option value=\"5\">1</option>\n");
      out.write("                                <option value=\"4\">2</option>                                \n");
      out.write("                                <option value=\"3\">3</option>                                \n");
      out.write("                                <option value=\"2\">4</option>                                \n");
      out.write("                                <option value=\"1\">5</option>                                \n");
      out.write("                            </select>\n");
      out.write("                        <p class=\"general\"></p>\n");
      out.write("\n");
      out.write("                        <p class=\"general\"><label for=\"bamount\">Manufacturing date\n");
      out.write("                            </label></p>\n");
      out.write("                        <input type=\"text\" id=\"bamount\" name=\"bamount\" readonly style=\"text-align: center; border:0;\">\n");
      out.write("                        <div id=\"slider-brange\"></div>\n");
      out.write("                        <p class=\"general\"><label for=\"bamountw\">Gravity\n");
      out.write("                            </label>\n");
      out.write("                            <select name=\"bamountw\" id=\"bamountw\"> \n");
      out.write("                                <option value=\"1\">1</option>\n");
      out.write("                                <option value=\"2\">2</option>                                \n");
      out.write("                                <option value=\"3\">3</option>                                \n");
      out.write("                                <option value=\"4\">4</option>                                \n");
      out.write("                                <option value=\"5\">5</option>                                \n");
      out.write("                            </select>\n");
      out.write("                        <p class=\"general\"></p>\n");
      out.write("\n");
      out.write("                        <p class=\"general\"><label for=\"samount\">Square meters\n");
      out.write("                            </label></p>\n");
      out.write("                        <input type=\"text\" id=\"samount\" name=\"samount\" readonly style=\"text-align: center; border:0;\">\n");
      out.write("                        <div id=\"slider-srange\"></div>\n");
      out.write("                        <p class=\"general\"><label for=\"samountw\">Gravity\n");
      out.write("                            </label>\n");
      out.write("                            <select name=\"samountw\" id=\"samountw\"> \n");
      out.write("                                <option value=\"1\">1</option>\n");
      out.write("                                <option value=\"2\">2</option>                                \n");
      out.write("                                <option value=\"3\">3</option>                                \n");
      out.write("                                <option value=\"4\">4</option>                                \n");
      out.write("                                <option value=\"5\">5</option>                                \n");
      out.write("                            </select>\n");
      out.write("\n");
      out.write("                        <p><input class=\"buttom\" name=\"submit\" id=\"submit\" value=\"search\" type=\"submit\"> </p>\t \n");
      out.write("                    </form>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"onerow\">\n");
      out.write("                <div class=\"col1\">             \n");
      out.write("                    <a href=\"index.jsp\"><img src=\"images/farfooter.png\" alt=\"logo\"/></a>\n");
      out.write("                </div>\n");
      out.write("                <div class=\"col3\"><p><b>Find A Residence &copy;</b><br>Support for Rent / Purchase Decisions</p></div>\n");
      out.write("                <div class=\"col4\">             \n");
      out.write("                    <p><b>Powered by:</b><br/>Infoideal technologies - 9812345648<br/>Office- 9812345648</p>\n");
      out.write("                </div>\n");
      out.write("                <div class=\"col4 last\">             \n");
      out.write("                    <p><a href=\"mailto:akashwaghmare54@gmail.com?Subject=Find A Residence\" target=\"_top\">Contact us</a></p>\n");
      out.write("                </div>\n");
      out.write("            </div> \n");
      out.write("        </div>\n");
      out.write("    </body>\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
