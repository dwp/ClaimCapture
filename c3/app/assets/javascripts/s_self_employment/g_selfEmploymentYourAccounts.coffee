window.initEvents = (answerY, answerN, tellUsText) ->

  if not $("#"+answerN).prop 'checked'
    hideSelfEmployedIncomeWrap(tellUsText)

  $("#" + answerY).on "click", ->
    hideSelfEmployedIncomeWrap(tellUsText)

  $("#" + answerN).on "click", ->
    showSelfEmployedIncomeWrap()

hideSelfEmployedIncomeWrap = (tellUsText) ->
	emptySelfEmployedIncomeWrap = ->
  		$("#"+tellUsText).val("")
  $("#selfEmployedIncomeWrap").slideUp(0, emptySelfEmployedIncomeWrap).attr 'aria-hidden', 'true'

showSelfEmployedIncomeWrap = ->
  $("#selfEmployedIncomeWrap").slideDown(0).attr 'aria-hidden', 'false'