window.initEvents = (answerY, answerN) ->
  $("#" + answerY).on "click", ->
    $("#selfEmployedPensionWrap").slideDown()
    $("#selfEmployedPensionWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#selfEmployedPensionWrap").slideUp()