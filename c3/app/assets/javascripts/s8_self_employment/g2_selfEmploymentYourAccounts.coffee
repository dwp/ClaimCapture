window.initEvents = (answerY, answerN, answerAccountantY, answerAccountantN) ->

  $("#" + answerY).on "click", ->
    $("#selfEmployedIncomeWrap").slideUp()

  $("#" + answerN).on "click", ->
    $("#selfEmployedIncomeWrap").slideDown()
    $("#selfEmployedIncomeWrap").css('display', "block")

  $("#" + answerAccountantY).on "click", ->
    $("#selfEmployedAccountantWrap").slideDown()
    $("#selfEmployedAccountantWrap").css('display', "block")

  $("#" + answerAccountantN).on "click", ->
    $("#selfEmployedAccountantWrap").slideUp()