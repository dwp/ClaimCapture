window.initEvents = (separatedFromPartnerIDYes,separatedFromPartnerIDNo)->
  $("#"+separatedFromPartnerIDYes).on "click", ->
    $("#separatedInfo").slideDown 500
    $("#separatedInfo").css('display',"block")
    
  $("#"+separatedFromPartnerIDNo).on "click", ->
    $("#separatedInfo").slideUp 500
