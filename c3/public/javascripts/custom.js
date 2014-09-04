var IE10 = (navigator.userAgent.match(/(MSIE 10.0)/g) ? true : false);
if (IE10) {
	$('html').addClass('ie10');
}
var IE11 = (navigator.userAgent.match(/(MSIE 11.0)/g) ? true : false);
if (IE11) {
	$('html').addClass('ie11');
}


// Chris' datepicker
function datepicker(dateFieldId) {
    return $(dateFieldId).datepicker({
        dateFormat: 'dd/mm/yy',
        showButtonPanel: true,
        closeText: 'Clear',
        onClose: function (dateText, inst) {
            if ($(window.event.srcElement).hasClass('ui-datepicker-close'))
            {
                document.getElementById(this.id).value = '';
            }
        }
    });
}


    
    
$(function() {    
    // view more / view less
$(".helper-info").css("display", "none");
$('.helper-more').click(function(){
   var labelText = $(this).text() === $(this).attr('data-close')? $(this).attr('data-initial') : $(this).attr('data-close');

        $(this).toggleClass("helper-less")
        $(this).next(".helper-info").slideToggle();

        $(this).text(labelText);
});    


// Add the "focus" value to class attribute 
  $('ul.radio li label').focusin( function() {
    $(this).addClass('focus');
  }
  );

  // Remove the "focus" value to class attribute 
  $('ul.radio li label').focusout( function() {
    $(this).removeClass('focus');
  }
  );




    
	// Help & Feedback container
     $(".feedback-container").css("display", "none");
     $('.feedback, .feed-close').click(function(){
     	$( ".feedback-container" ).toggle();
     });
     
     
     // Non JS message 
     $(".js-message").css("display", "none");
     $(".feed-close").css("display", "block");
     
    // Nino auto jump
	$('.ni-number input, .sort-code input').autotab_magic();

    // smooth scroll
    $('a[href^="#"]').bind('click.smoothscroll', function (e) {
        e.preventDefault();
        var target = this.hash,
            $target = $(target);
        $('html, body').animate({
            scrollTop: $(target).offset().top - 40
        }, 750, 'swing', function () {
            window.location.hash = target;
        });
    });

});

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

});


function areCookiesEnabled(){
    var cookieEnabled = (navigator.cookieEnabled) ? true : false;

    if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
    {
        document.cookie="testcookie";
        cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
    }
    return (cookieEnabled);
}

function trackEvent(category, action, label, value, noninteraction){
    _gaq.push(['_trackEvent',category,action].concat(opt(label)).concat(opt(value)).concat(opt(noninteraction)));
}

function trackVirtualPageView(category){
    _gaq.push(['_trackPageview',category]);
}

function opt(v){
    if (typeof v == 'undefined') return [];
    else return[v];
}