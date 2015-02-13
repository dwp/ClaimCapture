window.initEvents = (answer_yes, answer_no) ->
  $("#" + answer_yes).on "click", ->
    $("#answerNoMessageWrap").hide()

  $("#" + answer_no).on "click", ->
    $("#answerNoMessageWrap").show()
    $("#answerNoMessageWrap").css('display', "block")