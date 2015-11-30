
###
  isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

hideBenefitsFromEEADetailsWrapper = (benefitsFromEEADetails) ->
  val(benefitsFromEEADetails,"")
  S("benefitsFromEEADetailsWrapper").slideUp(0).attr 'aria-hidden', 'true'

showBenefitsFromEEADetailsWrapper = ->
  S("benefitsFromEEADetailsWrapper").slideDown(0).attr 'aria-hidden', 'false'
###

###
  $(document).ready(function(){
  $(".saveClaim").each(function(){
  var name=$(this).attr("name");
  var value=$(this).attr("value")
  $("form").find("input").each(function(){
    if( $(this).attr("type") == "radio" && $(this).attr("name") == name && $(this).attr("value") == value){
    $(this).click();
  }
  else if( $(this).attr("type") == "text" && $(this).attr("name") == name){
$(this).val(value);
  }
  });
  $("form").find("textarea").each(function(){
    if( $(this).attr("name") == name){
    $(this).val(value);
  }
  });
  });
});

###


