$(document).ready(function() {

// view more / view less
    $('.helper-more').click(function(){
       $(this).toggleClass("helper-less");
       $('.helper-info').slideToggle('slow');
       if($(this).text() === 'Close')
       {
           $(this).text('Show example');
       }
       else
       {
       $(this).text('Close');
       }
	 });
});
