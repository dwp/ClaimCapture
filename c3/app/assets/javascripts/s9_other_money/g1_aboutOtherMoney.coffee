window.initEvents = (answerY, answerN, whoPaysYou, howMuch, howOften_frequency, howOften_frequency_other) ->
  $("#" + answerY).on "click", ->
    $("#otherPayWrap").slideDown()
    $("#otherPayWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#otherPayWrap").slideUp( ->
      $("#"+whoPaysYou).val("")
      $("#"+howMuch).val("")
      $("#"+howOften_frequency).val("")
      $("#"+howOften_frequency_other).val(""))
