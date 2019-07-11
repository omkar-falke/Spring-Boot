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
        <link href="css/style.css" rel="stylesheet" type="text/css"/>
        <script src="javascript/scripts.js" type="text/javascript"></script>
        <title>Find A Residence - Home</title>	
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
                                    <li><a href='myproperties.jsp'>My Properties</a></li> <%}%>
                                    <li><a href='editprofile.jsp'>Edit Profile</a></li>
                                    <li><a href='logout.jsp'>LOGOUT</a></li>
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
                <div id="login" style="display:none" >
                    <button type="button" onclick="popup('login')">x</button>
                    <form id="login-form" action="login" method="POST">
                        <input type="text" placeholder="Username" name="username"/>
                        <input type="password" placeholder="Code" name="password" />
                        <input type="submit" value="Connection" />
                    </form>
                </div>
            </div>
            <div class="onerow">
                <div class="col12 last">
                    <div style="overflow: hidden; padding-top:25px;" >
                        <figure id="slidy">
                            <img src="images/slider/slide1.jpg" alt/>
                            <img src="images/slider/slide2.jpg" alt/>
                            <img src="images/slider/slide3.jpg" alt/>
                            <img src="images/slider/slide4.jpg" alt/>
                            <img src="images/slider/slide5.jpg" alt/>
                            <img src="images/slider/slide6.jpg" alt/>
                            <img src="images/slider/slide7.jpg" alt/>
                            <img src="images/slider/slide8.jpg" alt/>
                            <img src="images/slider/slide9.jpg" alt/>
                        </figure>
                    </div>
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
        <script>
            var timeOnSlide = 3,
                    timeBetweenSlides = 1.5,
                    animationstring = 'animation',
                    animation = false,
                    keyframeprefix = '',
                    domPrefixes = 'Webkit Moz O Khtml'.split(' '),
                    pfx = '',
                    slidy = document.getElementById("slidy");
            if (slidy.style.animationName !== undefined) {
                animation = true;
            }
            if (animation === false) {
                for (var i = 0; i < domPrefixes.length; i++) {
                    if (slidy.style[ domPrefixes[i] + 'AnimationName' ] !== undefined) {
                        pfx = domPrefixes[ i ];
                        animationstring = pfx + 'Animation';
                        keyframeprefix = '-' + pfx.toLowerCase() + '-';
                        animation = true;
                        break;
                    }
                }
            }

            if (animation === false) {
                // animate using a JavaScript fallback, if you wish
            } else {
                var images = slidy.getElementsByTagName("img"),
                        firstImg = images[0],
                        imgWrap = firstImg.cloneNode(false);
                slidy.appendChild(imgWrap);
                var imgCount = images.length,
                        totalTime = (timeOnSlide + timeBetweenSlides) * (imgCount - 1),
                        slideRatio = (timeOnSlide / totalTime) * 100,
                        moveRatio = (timeBetweenSlides / totalTime) * 100,
                        basePercentage = 100 / imgCount,
                        position = 0,
                        css = document.createElement("style");
                css.type = "text/css";
                css.innerHTML += "#slidy { text-align: left; margin: 0; font-size: 0; position: relative; width: " + (imgCount * 100) + "%;  }";
                css.innerHTML += "#slidy img { float: left; width: " + basePercentage + "%; }";
                css.innerHTML += "@" + keyframeprefix + "keyframes slidy {";
                for (i = 0; i < (imgCount - 1); i++) {
                    position += slideRatio;
                    css.innerHTML += position + "% { left: -" + (i * 100) + "%; }";
                    position += moveRatio;
                    css.innerHTML += position + "% { left: -" + ((i + 1) * 100) + "%; }";
                }
                css.innerHTML += "}";
                css.innerHTML += "#slidy { left: 0%; " + keyframeprefix + "transform: translate3d(0,0,0); " + keyframeprefix + "animation: " + totalTime + "s slidy infinite; }";
                document.body.appendChild(css);
            }
        </script>
    </body>
</html>