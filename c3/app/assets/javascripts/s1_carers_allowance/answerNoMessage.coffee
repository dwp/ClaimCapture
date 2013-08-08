window.initEvents = (answer_yes, answer_no) ->
    $("#" + answer_yes).on "click", ->
      $("#answerNoMessageWrap").slideUp()

    $("#" + answer_no).on "click", ->
      $("#answerNoMessageWrap").slideDown()
      $("#answerNoMessageWrap").css('display', "block")