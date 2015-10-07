isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents = (o) ->
  hideDoYouKnowYourTradingYearWrap = () ->
    S("doYouKnowYourTradingYearIncomeWrap").slideUp(0).attr 'aria-hidden', 'true'

  showDoYouKnowYourTradingYearWrap = () ->
    S("doYouKnowYourTradingYearIncomeWrap").slideDown(0).attr 'aria-hidden', 'false'

  if not isChecked(o.doYouKnowYourTradingYearY)
    hideDoYouKnowYourTradingYearWrap()

  S(o.doYouKnowYourTradingYearN).on "click", ->
    hideDoYouKnowYourTradingYearWrap()

  S(o.doYouKnowYourTradingYearY).on "click", ->
    showDoYouKnowYourTradingYearWrap()

  hideSelfEmployedIncomeWrap = (o) ->
    emptySelfEmployedIncomeWrap = () -> S(o.tellUsWhyAndWhenTheChangeHappened).val("")
    S("selfEmployedIncomeWrap").slideUp(0, emptySelfEmployedIncomeWrap).attr 'aria-hidden', 'true'

  showSelfEmployedIncomeWrap = () ->
    S("selfEmployedIncomeWrap").slideDown(0).attr 'aria-hidden', 'false'

  if not isChecked(o.areIncomeOutgoingsProfitSimilarToTradingN)
    hideSelfEmployedIncomeWrap(o)

  S(o.areIncomeOutgoingsProfitSimilarToTradingY).on "click", ->
    hideSelfEmployedIncomeWrap(o)

  S(o.areIncomeOutgoingsProfitSimilarToTradingN).on "click", ->
    showSelfEmployedIncomeWrap()

