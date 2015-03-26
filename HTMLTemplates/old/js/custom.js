$(document).ready(function() {

// view more / view less
    $('.helper-more').click(function(){
       $(this).toggleClass("helper-less");
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



$.extend($.datepicker, {
         _doKeyDown: function(event){
               console.log("test");
         }
    });
    

$("#start-date, #end-date").datepicker({
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
    
    
 $('.feedback, .feed-close').click(function(){
       $(this).toggleClass("feedback-close");
       $('.feedback-container').slideToggle('slow');
  
     });  

	 
	 
});
