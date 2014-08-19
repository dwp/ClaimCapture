window.initEvents = (answerY, answerN) ->

  $("#" + answerY).on "click", ->
    $("#selfEmployedIncomeWrap").slideUp 0

  $("#" + answerN).on "click", ->
    $("#selfEmployedIncomeWrap").slideDown 0
    $("#selfEmployedIncomeWrap").css('display', "block")
