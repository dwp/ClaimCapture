window.initialButtonState = (answerY, answerN) ->
  if (!$("#" + answerY).prop('checked') && !$("#" + answerN).prop('checked'))
    $("#showAppropriateButtonWrap").css('display', "none")

  if ($("#" + answerN).prop('checked'))
    $("#actionButtonId").text("Finished")
    $("#showAppropriateButtonWrap").css('display', "block")

  if ($("#" + answerY).prop('checked'))
    $("#actionButtonId").text("Continue")
    $("#showAppropriateButtonWrap").css('display', "block")


window.showAppropriateButton = (answerY, answerN) ->
  $("#" + answerN).on "click", ->
    $("#actionButtonId").text("Finished")
    $("#showAppropriateButtonWrap").css('display', "block")

  $("#" + answerY).on "click", ->
    $("#actionButtonId").text("Continue")
    $("#showAppropriateButtonWrap").css('display', "block")
