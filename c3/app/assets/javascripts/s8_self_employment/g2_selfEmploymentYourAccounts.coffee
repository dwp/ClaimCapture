window.initEvents = (answerY, answerN) ->

  if not $("#"+answerN).prop 'checked'
    hideSelfEmployedIncomeWrap()

  $("#" + answerY).on "click", ->
    hideSelfEmployedIncomeWrap()

  $("#" + answerN).on "click", ->
    showSelfEmployedIncomeWrap()

hideSelfEmployedIncomeWrap = ->
  $("#selfEmployedIncomeWrap").slideUp 0

showSelfEmployedIncomeWrap = ->
  $("#selfEmployedIncomeWrap").slideDown 0