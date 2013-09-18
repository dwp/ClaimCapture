$(document).ready(function() {

    // view more / view less
    $('.helper-more').click(function(){
       $(this).toggleClass("helper-less")
       $(this).next(".helper-info").slideToggle("medium");

       if($(this).text() === 'Close')
       {
           $(this).text('Show example');
       }
       else
       {
        $(this).text('Close');
       }
	 });

    // smooth scroll
    $('a[href^="#"]').bind('click.smoothscroll',function (e) {
        e.preventDefault();
        var target = this.hash,
            $target = $(target);
        $('html, body').animate({
            scrollTop: $(target).offset().top - 40
        }, 750, 'swing', function () {
            window.location.hash = target;
        });
    });

    $('.datepicker').pickadate({
        selectYears: true,
        selectMonths: true
    });
});