window.initEvents = (answerY, answerN) ->

  $("#" + answerY).on "click", ->
    $("#selfEmployedIncomeWrap").slideUp()

  $("#" + answerN).on "click", ->
    $("#selfEmployedIncomeWrap").slideDown()
    $("#selfEmployedIncomeWrap").css('display', "block")
