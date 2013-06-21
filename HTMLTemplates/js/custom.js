$(document).ready(function() {

// date picker
$('.date').each(function() {
        $(this).datepicker({ dateFormat: 'dd-mm-yy' });
    });
    
// content help    
$(".helper-more").click(function(){
        $(".helper-info").slideToggle("slow");
   	});    

// smooth scroll    
$('a[href^="#"]').bind('click.smoothscroll',function (e) {
    e.preventDefault();
 
    var target = this.hash,
        $target = $(target);
 
    $('html, body').stop().animate({
        'scrollTop': $target.offset().top
    }, 1000, 'swing', function () {
        window.location.hash = target;
    });
});    
    
    
});
