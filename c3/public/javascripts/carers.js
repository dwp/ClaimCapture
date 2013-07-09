$(document).ready(function() {
    if (typeof console !== 'undefined') {
        console.log("document on ready " + areCookiesEnabled() );
    }

    if (areCookiesEnabled())  {
        $('#no-cookies').toggle(false);
        $('#content').toggle(true);
    }else{
        $('#no-cookies').toggle(true);
        $('#content').toggle(false);
    }

    if ($(".completed li:last").length > 0 && $(".completed li:last").offset() != null){
        $("html, body").animate({
            scrollTop: $(".completed li:last").offset().top+$(".completed li:last").height()
        }, 2000);
    }

});


function areCookiesEnabled()
{
	var cookieEnabled = (navigator.cookieEnabled) ? true : false;

	if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
	{
		document.cookie="testcookie";
		cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
	}
	return (cookieEnabled);
}