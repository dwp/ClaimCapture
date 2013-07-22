window.initEvents = (haveYouHadAnyStatutorySickPayY, haveYouHadAnyStatutorySickPayN) ->
  $("#" + haveYouHadAnyStatutorySickPayY).on "click", ->
    $("#sickPayWrap").slideDown 500
    $("#sickPayWrap").css('display', "block")
    
  $("#" + haveYouHadAnyStatutorySickPayN).on "click", ->
    $("#sickPayWrap").slideUp 500