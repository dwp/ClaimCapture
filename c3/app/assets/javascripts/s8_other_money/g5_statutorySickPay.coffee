window.initEvents = (haveYouHadAnyStatutorySickPayY, haveYouHadAnyStatutorySickPayN, howOften_frequency, howOften_frequency_wrap) ->
  $("#" + haveYouHadAnyStatutorySickPayY).on "click", ->
    $("#sickPayWrap").slideDown 500
    $("#sickPayWrap").css('display', "block")
    
  $("#" + haveYouHadAnyStatutorySickPayN).on "click", ->
    $("#sickPayWrap").slideUp 500
      
  $("#" + howOften_frequency).on "change", ->
    selected = $("#" + howOften_frequency + " option").filter(':selected').text()
    if selected is "Other"
      #document.write("selected is: " + selected)
      $("#" + howOften_frequency_wrap).slideDown()
      $("#" + howOften_frequency_wrap).css('display', "block")
    else 
      $("#" + howOften_frequency_wrap).slideUp()