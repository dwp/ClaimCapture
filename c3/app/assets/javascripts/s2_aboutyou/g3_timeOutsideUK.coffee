window.initEvents = (currentlyLivingUKIDY,currentlyLivingUKIDN,planGoBackY,planGoBackN)->
  $("#"+currentlyLivingUKIDY).on "click", ->
    $("#livingInUK").slideDown 500
  $("#"+currentlyLivingUKIDN).on "click", ->
    $("#livingInUK").slideUp 500

  $("#"+planGoBackY).on "click", ->
    $("#planingToGoBack").slideDown 500

  $("#"+planGoBackN).on "click", ->
    $("#planingToGoBack").slideUp 500