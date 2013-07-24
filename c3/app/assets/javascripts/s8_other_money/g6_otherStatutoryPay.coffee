window.initEvents = (answerY, answerN, howOften_frequency, howOften_frequency_wrap) ->
    $("#" + answerY).on "click", ->
      $("#otherPayWrap").slideDown()
      $("#otherPayWrap").css('display', "block")

    $("#" + answerN).on "click", ->
      $("#otherPayWrap").slideUp()
      
    $("#" + howOften_frequency).on "click", ->
      selected = $("#" + howOften_frequency + " option").filter(':selected').text()
      if selected is "Other"
        $("#" + howOften_frequency_wrap).slideDown()
        $("#" + howOften_frequency_wrap).css('display', "block")
      else 
        $("#" + howOften_frequency_wrap).slideUp()