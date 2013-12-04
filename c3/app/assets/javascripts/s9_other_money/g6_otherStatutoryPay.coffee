window.initEvents = (answerY, answerN, howOften_frequency, howOften_frequency_wrap) ->
  $("#" + answerY).on "click", ->
    $("#otherPayWrap").slideDown()
    $("#otherPayWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#otherPayWrap").slideUp()