(function($) {

    $.fn.menumaker = function(options) {

        var cssmenu = $(this), settings = $.extend({
            title: "Menu",
            format: "dropdown",
            sticky: false
        }, options);

        return this.each(function() {
            cssmenu.prepend('<div id="menu-button">' + settings.title + '</div>');
            $(this).find("#menu-button").on('click', function() {
                $(this).toggleClass('menu-opened');
                var mainmenu = $(this).next('ul');
                if (mainmenu.hasClass('open')) {
                    mainmenu.hide().removeClass('open');
                }
                else {
                    mainmenu.show().addClass('open');
                    if (settings.format === "dropdown") {
                        mainmenu.find('ul').show();
                    }
                }
            });

            cssmenu.find('li ul').parent().addClass('has-sub');

            multiTg = function() {
                cssmenu.find(".has-sub").prepend('<span class="submenu-button"></span>');
                cssmenu.find('.submenu-button').on('click', function() {
                    $(this).toggleClass('submenu-opened');
                    if ($(this).siblings('ul').hasClass('open')) {
                        $(this).siblings('ul').removeClass('open').hide();
                    }
                    else {
                        $(this).siblings('ul').addClass('open').show();
                    }
                });
            };

            if (settings.format === 'multitoggle')
                multiTg();
            else
                cssmenu.addClass('dropdown');

            if (settings.sticky === true)
                cssmenu.css('position', 'fixed');

            resizeFix = function() {
                if ($(window).width() > 768) {
                    cssmenu.find('ul').show();
                }

                if ($(window).width() <= 768) {
                    cssmenu.find('ul').hide().removeClass('open');
                }
            };
            resizeFix();
            return $(window).on('resize', resizeFix);

        });
    };
})(jQuery);

(function($) {
    $(document).ready(function() {

        $("#cssmenu").menumaker({
            title: "Menu",
            format: "multitoggle"
        });

    });
})(jQuery);


function toggle(div_id) {
    var el = document.getElementById(div_id);
    if (el.style.display === 'none') {
        el.style.display = 'block';
    }
    else {
        el.style.display = 'none';
    }
}
function blanket_size(popUpDivVar) {
    if (typeof window.innerWidth !== 'undefined') {
        viewportheight = window.innerHeight;
    } else {
        viewportheight = document.documentElement.clientHeight;
    }
    if ((viewportheight > document.body.parentNode.scrollHeight) && (viewportheight > document.body.parentNode.clientHeight)) {
        blanket_height = viewportheight;
    } else {
        if (document.body.parentNode.clientHeight > document.body.parentNode.scrollHeight) {
            blanket_height = document.body.parentNode.clientHeight;
        } else {
            blanket_height = document.body.parentNode.scrollHeight;
        }
    }
    var blanket = document.getElementById('blanket');
    blanket.style.height = blanket_height + 'px';
    var popUpDiv = document.getElementById(popUpDivVar);
    popUpDiv_height = blanket_height / 2 - 200;//200 is half popup's height
    popUpDiv.style.top = popUpDiv_height + 'px';
}
function window_pos(popUpDivVar) {
    if (typeof window.innerWidth !== 'undefined') {
        viewportwidth = window.innerHeight;
    } else {
        viewportwidth = document.documentElement.clientHeight;
    }
    if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
        window_width = viewportwidth;
    } else {
        if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
            window_width = document.body.parentNode.clientWidth;
        } else {
            window_width = document.body.parentNode.scrollWidth;
        }
    }
    var popUpDiv = document.getElementById(popUpDivVar);
    window_width = window_width / 2 - 200;//200 is half popup's width
    popUpDiv.style.left = window_width + 'px';
}
function popup(windowname) {
    blanket_size(windowname);
    window_pos(windowname);
    toggle('blanket');
    toggle(windowname);
}

function validateRegisterForm() {
    var x = document.forms["register-form"]["username"].value;
    if (x === null || x === "") {
        alert("Username is empty.");
        return false;
    }
    if (x.length < 3) {
        alert("User Name is less than the allowed length.");
        return false;
    }
    if (x.length > 20) {
        alert("User Name is longer than the allowed length.");
        return false;
    }
    var y = document.forms["register-form"]["password"].value;
    if (y === null || y === "") {
        alert("Password is empty");
        return false;
    }
    if (y.length < 5) {
        alert("The Password is less than the permissible length.");
        return false;
    }
    if (y.length > 15) {
        alert("The Password has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["register-form"]["password2"].value;
    if (x !== y) {
        alert("The Confirmation Password does not match.");
        return false;
    }
    var x = document.forms["register-form"]["email"].value;
    if (x === null || x === "") {
        alert("Email is empty");
        return false;
    }
    if (x.length > 254) {
        alert("Email has a longer than allowed length.");
        return false;
    }
    var atpos = x.indexOf("@");
    var dotpos = x.lastIndexOf(".");
    if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= x.length) {
        alert("Invalid Email.");
        return false;
    }
    var x = document.forms["register-form"]["name"].value;
    if (x === null || x === "") {
        alert("Name is empty.");
        return false;
    }
    if (x.length > 50) {
        alert("The Name is longer than the allowed length.");
        return false;
    }
    var x = document.forms["register-form"]["surname"].value;
    if (x === null || x === "") {
        alert("Surname is empty.");
        return false;
    }
    if (x.length > 50) {
        alert("The hatch has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["register-form"]["address1"].value;
    if (x === null || x === "") {
        alert("Address 1 is empty.");
        return false;
    }
    if (x.length > 64) {
        alert("Address 1 has a longer than the allowed length.");
        return false;
    }
    var x = document.forms["register-form"]["city"].value;
    if (x === null || x === "") {
        alert("City is empty.");
        return false;
    }
    if (x.length > 64) {
        alert("The City has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["register-form"]["region"].value;
    if (x === null || x === "") {
        alert("The Area is empty");
        return false;
    }
    if (x.length > 64) {
        alert("The Area has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["register-form"]["postalCode"].value;
    if (x === null || x === "") {
        alert("Postcode is empty");
        return false;
    }
    if (x.length > 8) {
        alert("The postal code has a longer than the permissible length.");
        return false;
    }
}


function validateEditProfileForm() {
    var x = document.forms["form"]["email"].value;
    if (x === null || x === "") {
        alert("Email is empty");
        return false;
    }
    if (x.length > 254) {
        alert("Email has a longer than allowed length.");
        return false;
    }
    var atpos = x.indexOf("@");
    var dotpos = x.lastIndexOf(".");
    if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= x.length) {
        alert("Invalid Email.");
        return false;
    }
    var x = document.forms["form"]["name"].value;
    if (x === null || x === "") {
        alert("Name is empty.");
        return false;
    }
    if (x.length > 50) {
        alert("The Name is longer than the allowed length.");
        return false;
    }
    var x = document.forms["form"]["surname"].value;
    if (x === null || x === "") {
        alert("Surname is empty.");
        return false;
    }
    if (x.length > 50) {
        alert("The hatch has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["form"]["address1"].value;
    if (x === null || x === "") {
        alert("Address 1 is empty.");
        return false;
    }
    if (x.length > 64) {
        alert("Address 1 has a longer than the allowed length.");
        return false;
    }
    var x = document.forms["form"]["city"].value;
    if (x === null || x === "") {
        alert("City is empty.");
        return false;
    }
    if (x.length > 64) {
        alert("The City has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["form"]["region"].value;
    if (x === null || x === "") {
        alert("The Area is empty");
        return false;
    }
    if (x.length > 64) {
        alert("The Area has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["form"]["postalCode"].value;
    if (x === null || x === "") {
        alert("Postcode is empty");
        return false;
    }
    if (x.length > 8) {
        alert("The postal code has a longer than the permissible length.");
        return false;
    }
}

function validateChangePasswordForm() {
    var y = document.forms["form"]["oldpassword"].value;
    if (y === null || y === "") {
        alert("Old Password is empty");
        return false;
    }
    if (y.length < 5) {
        alert("The Old Password has less than the permissible length.");
        return false;
    }
    if (y.length > 15) {
        alert("The Old Password has a longer than the permissible length.");
        return false;
    }
    var y = document.forms["form"]["newpassword"].value;
    if (y === null || y === "") {
        alert("New Password is empty");
        return false;
    }
    if (y.length < 5) {
        alert("The New Password has less than the permissible length.");
        return false;
    }
    if (y.length > 15) {
        alert("The New Password has a longer than the allowed length.");
        return false;
    }
    var x = document.forms["form"]["renewpassword"].value;
    if (x !== y) {
        alert("The Confirmation Password is not the same as the New Password.");
        return false;
    }
}

function validateImages(thisform)
{
    with (thisform)
    {
        if (document.getElementById("picture1").value !== "") {
            if (validateFileExtension(picture1, new Array("jpg", "jpeg", "gif", "png")) === false)
            {
                alert("Unacceptable file type.");
                return false;
            }
            if (validateFileSize(picture1, 1048576) === false)
            {
                alert("Unacceptable file size.");
                return false;
            }
        }
        if (document.getElementById("picture2").value !== "") {

            if (validateFileExtension(picture2, new Array("jpg", "jpeg", "gif", "png")) === false)
            {
                alert("Unacceptable file type.");
                return false;
            }
            if (validateFileSize(picture2, 1048576) === false)
            {
                alert("Unacceptable file size.");
                return false;
            }
        }
        if (document.getElementById("picture3").value !== "") {

            if (validateFileExtension(picture3, new Array("jpg", "jpeg", "gif", "png")) === false)
            {
                alert("Unacceptable file type.");
                return false;
            }
            if (validateFileSize(picture3, 1048576) === false)
            {
                alert("Unacceptable file size.");
                return false;
            }
        }
        if (document.getElementById("picture4").value !== "") {

            if (validateFileExtension(picture4, new Array("jpg", "jpeg", "gif", "png")) === false)
            {
                alert("Unacceptable file types");
                return false;
            }
            if (validateFileSize(picture4, 1048576) === false)
            {
                alert("Unacceptable file size.");
                return false;
            }
        }
        if (document.getElementById("picture5").value !== "") {

            if (validateFileExtension(picture5, new Array("jpg", "jpeg", "gif", "png")) === false)
            {
                alert("Unacceptable file type.");
                return false;
            }
            if (validateFileSize(picture5, 1048576) === false)
            {
                alert("Unacceptable file size.");
                return false;
            }
        }
    }
}


function validateImage(thisform)
{
    with (thisform)
    {
        if (validateFileExtension(picture, new Array("jpg", "jpeg", "gif", "png")) === false)
        {
            alert("Unacceptable file type.");
            return false;
        }
        if (validateFileSize(picture, 1048576) === false)
        {
            alert("Unacceptable file size.");
            return false;
        }
    }
}

function validateFileExtension(component, extns)
{
    var flag = 0;
    with (component)
    {
        var ext = value.substring(value.lastIndexOf('.') + 1);
        for (i = 0; i < extns.length; i++)
        {
            if (ext === extns[i])
            {
                flag = 0;
                break;
            }
            else
            {
                flag = 1;
            }
        }
        if (flag !== 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

function validateFileSize(component, maxSize)
{
    if (navigator.appName === "Microsoft Internet Explorer")
    {
        if (component.value)
        {
            var oas = new ActiveXObject("Scripting.FileSystemObject");
            var e = oas.getFile(component.value);
            var size = e.size;
        }
    }
    else
    {
        if (component.files[0] !== undefined)
        {
            size = component.files[0].size;
        }
    }
    if (size !== undefined && size > maxSize)
    {
        return false;
    }
    else
    {
        return true;
    }
}

function countdownindex(num) {
    if (num > 0) {
        setTimeout("countdownindex(" + (num - 1) + ")", 1000);
        document.getElementById("countdownindex").innerHTML = num + " seconds";
    }
    if (num < 2) {
        document.getElementById("countdownindex").innerHTML = num + " seconds";
    }
    if (num < 1) {
        window.location.replace("index.jsp");
        document.getElementById("countdownindex").innerHTML = num + " seconds";
    }
}

function countdownregister(num) {
    if (num > 0) {
        setTimeout("countdownregister(" + (num - 1) + ")", 1000);
        document.getElementById("countdownregister").innerHTML = num + " seconds";
    }
    if (num < 2) {
        document.getElementById("countdownregister").innerHTML = num + " seconds";
    }
    if (num < 1) {
        window.location.replace("register.jsp");
        document.getElementById("countdownregister").innerHTML = num + " seconds";
    }
}


function validatePropertyForm() {
    var x = document.forms["pform"]["price"].value;
    if (x === null || x === "") {
        alert("You have not set a Price.");
        return false;
    }
    var x = document.forms["pform"]["maintenanceCharges"].value;
    if (x === null || x === "") {
        alert("You have not set Maintenance Costs.");
        return false;
    }
    var x = document.forms["pform"]["sqMeters"].value;
    if (x === null || x === "") {
        alert("You have not defined Square Meters.");
        return false;
    }
    var x = document.forms["pform"]["roomsNo"].value;
    if (x === null || x === "") {
        alert("You have not defined a Number of Rooms.");
        return false;
    }
    var x = document.forms["pform"]["address1"].value;
    if (x === null || x === "") {
        alert("Address 1 is empty.");
        return false;
    }
    if (x.length > 64) {
        alert("Address 1 is longer than the allowed length.");
        return false;
    }
    var x = document.forms["pform"]["city"].value;
    if (x === null || x === "") {
        alert("City is empty.");
        return false;
    }
    if (x.length > 64) {
        alert("The City has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["pform"]["region"].value;
    if (x === null || x === "") {
        alert("The Area is empty");
        return false;
    }
    if (x.length > 64) {
        alert("The Area has a longer than the permissible length.");
        return false;
    }
    var x = document.forms["pform"]["postalCode"].value;
    if (x === null || x === "") {
        alert("Postcode is empty");
        return false;
    }
    if (x.length > 8) {
        alert("The postal code has a longer than the permissible length.");
        return false;
    }
}
