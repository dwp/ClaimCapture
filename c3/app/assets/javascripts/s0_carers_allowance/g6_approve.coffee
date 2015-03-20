window.initialButtonState = (answerY, answerN) ->
  if (!$("#" + answerY).prop('checked') && !$("#" + answerN).prop('checked'))
    $("#backButtonWrap").css('display', "none")
    $("#showAppropriateButtonWrap").css('display', "none")

  if ($("#" + answerN).prop('checked'))
    $("#actionButtonId").text("Finished")
    $("#backButtonWrap").css('display', "none")
    $("#showAppropriateButtonWrap").css('display', "block")

  if ($("#" + answerY).prop('checked'))
    $("#actionButtonId").text("Continue")
    $("#backButtonWrap").css('display', "block")
    $("#showAppropriateButtonWrap").css('display', "block")


window.answer = (answerY, answerN) ->
  $("#" + answerN).on "click", ->
    $("#actionButtonId").text("Finished")
    $("#backButtonWrap").css('display', "none")
    $("#showAppropriateButtonWrap").css('display', "block")

  $("#" + answerY).on "click", ->
    $("#actionButtonId").text("Continue")
    $("#backButtonWrap").css('display', "block")
    $("#showAppropriateButtonWrap").css('display', "block")
