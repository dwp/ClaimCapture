window.initialButtonState = (answerY, answerN) ->
  if (!$("#" + answerY).prop('checked') && !$("#" + answerN).prop('checked'))
    $("#showAppropriateButtonWrap").css('display', "none")

  if ($("#" + answerN).prop('checked'))
    $("#actionButtonId").css('display', "none")
    $("#feedbackLinkId").css('display', "block")
    $("#showAppropriateButtonWrap").css('display', "block")

  if ($("#" + answerY).prop('checked'))
    $("#actionButtonId").css('display', "block")
    $("#feedbackLinkId").css('display', "none")
    $("#showAppropriateButtonWrap").css('display', "block")


window.answer = (answerY, answerN) ->
  $("#" + answerN).on "click", ->
    $("#actionButtonId").css('display', "none")
    $("#feedbackLinkId").css('display', "block")
    $("#showAppropriateButtonWrap").css('display', "block")

  $("#" + answerY).on "click", ->
    $("#actionButtonId").css('display', "block")
    $("#feedbackLinkId").css('display', "none")
    $("#showAppropriateButtonWrap").css('display', "block")
