window.initEvents = (haveYouHadAnyStatutorySickPayY, haveYouHadAnyStatutorySickPayN, howOften_frequency, howOften_frequency_wrap) ->
  $("#" + haveYouHadAnyStatutorySickPayY).on "click", ->
    $("#sickPayWrap").slideDown 500
    $("#sickPayWrap").css('display', "block")
    
  $("#" + haveYouHadAnyStatutorySickPayN).on "click", ->
    $("#sickPayWrap").slideUp 500