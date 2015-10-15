isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")
label = (selector) -> $("label[for='#{selector}']")

window.initEvents = (o) ->
  hideDoYouKnowYourTradingYearWrap = () ->
    S("doYouKnowYourTradingYearIncomeWrap").slideUp(0).attr 'aria-hidden', 'true'

  showDoYouKnowYourTradingYearWrap = () ->
    S("doYouKnowYourTradingYearIncomeWrap").slideDown(0).attr 'aria-hidden', 'false'

  if not isChecked(o.doYouKnowYourTradingYearY)
    hideDoYouKnowYourTradingYearWrap()

  S(o.doYouKnowYourTradingYearN).on "click", ->
    hideDoYouKnowYourTradingYearWrap()
    resetData(o)

  S(o.doYouKnowYourTradingYearY).on "click", ->
    showDoYouKnowYourTradingYearWrap()

  hideSelfEmployedIncomeWrap = (o) ->
    emptySelfEmployedIncomeWrap = -> S(o.change).val("")
    S("selfEmployedIncomeWrap").slideUp(0, emptySelfEmployedIncomeWrap).attr 'aria-hidden', 'true'

  showSelfEmployedIncomeWrap = ->
    S("selfEmployedIncomeWrap").slideDown(0).attr 'aria-hidden', 'false'

  if not isChecked(o.similarN)
    hideSelfEmployedIncomeWrap(o)

  S(o.similarY).on "click", ->
    hideSelfEmployedIncomeWrap(o)

  S(o.similarN).on "click", ->
    showSelfEmployedIncomeWrap()

  resetData = (o) ->
    hideSelfEmployedIncomeWrap(o)
    S(o.similarY).prop('checked',false)
    S(o.similarN).prop('checked',false)
    label(o.similarY).removeClass("selected")
    label(o.similarN).removeClass("selected")
    S(o.toDay).val("")
    S(o.toMonth).val("")
    S(o.toYear).val("")
    S(o.fromDay).val("")
    S(o.fromMonth).val("")
    S(o.fromYear).val("")
