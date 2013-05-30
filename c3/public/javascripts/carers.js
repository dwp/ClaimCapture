$(document).ready(function() {
    console.log("document on ready " + areCookiesEnabled() );

    if (areCookiesEnabled())  {
        $('#no-cookies').toggle(false);
        $('#content').toggle(true);
    }
    else
    {
        $('#no-cookies').toggle(true);
        $('#content').toggle(false);
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