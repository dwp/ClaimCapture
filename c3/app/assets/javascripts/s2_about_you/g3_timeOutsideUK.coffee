window.initEvents = (currentlyLivingUKIDY,currentlyLivingUKIDN,planGoBackY,planGoBackN)->
  $("#"+currentlyLivingUKIDY).on "click", ->
    $("#livingInUK").slideDown 500
    $("#livingInUK").css('display',"block")
    
  $("#"+currentlyLivingUKIDN).on "click", ->
    $("#livingInUK").slideUp 500

  $("#"+planGoBackY).on "click", ->
    $("#planingToGoBack").slideDown 500
    $("#planingToGoBack").css('display',"block")

  $("#"+planGoBackN).on "click", ->
    $("#planingToGoBack").slideUp 500