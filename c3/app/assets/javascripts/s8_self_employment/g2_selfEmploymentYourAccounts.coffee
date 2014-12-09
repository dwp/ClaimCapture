window.initEvents = (answerY, answerN, tellUsText) ->

  if not $("#"+answerN).prop 'checked'
    hideSelfEmployedIncomeWrap(tellUsText)

  $("#" + answerY).on "click", ->
    hideSelfEmployedIncomeWrap(tellUsText)

  $("#" + answerN).on "click", ->
    showSelfEmployedIncomeWrap()

hideSelfEmployedIncomeWrap = (tellUsText) ->
  $("#"+tellUsText).val("")
  $("#selfEmployedIncomeWrap").slideUp 0

showSelfEmployedIncomeWrap = ->
  $("#selfEmployedIncomeWrap").slideDown 0