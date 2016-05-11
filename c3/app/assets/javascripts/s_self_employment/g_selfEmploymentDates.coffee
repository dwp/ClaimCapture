window.initEvents = (answerY, answerN, noWrap,
                     moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap,
                     haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,
                     tradingYearY, tradingYearN, tradingYearYWrap,
                      paidMoneyY, paidMoneyN, paidMoneyYWrap) ->

  setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)

  $("#" + answerY).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + answerN).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + moreThanYearY).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + moreThanYearN).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + haveAccountsY).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + haveAccountsN).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + tradingYearY).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + tradingYearN).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + paidMoneyY).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)
  $("#" + paidMoneyN).on "click", ->
    setVisibility(  answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)


setVisibility = (answerY, answerN, noWrap, moreThanYearY, moreThanYearN, moreThanYearYWrap, moreThanYearNWrap, haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,tradingYearY, tradingYearN, tradingYearYWrap, paidMoneyY, paidMoneyN, paidMoneyYWrap)->
  if $("#"+answerN).prop 'checked'
    showWrapper(noWrap)
  else
    hideWrapper(noWrap)

  if $("#"+moreThanYearY).prop 'checked'
    showWrapper(moreThanYearYWrap)
  else
    hideWrapper(moreThanYearYWrap)

  if $("#"+haveAccountsY).prop 'checked'
    showWrapper(haveAccountsYWrap)
  else
    hideWrapper(haveAccountsYWrap)

  if $("#"+haveAccountsN).prop 'checked'
    showWrapper(haveAccountsNWrap)
  else
    hideWrapper(haveAccountsNWrap)

  if $("#"+tradingYearY).prop 'checked'
    showWrapper(tradingYearYWrap)
  else
    hideWrapper(tradingYearYWrap)

  if $("#"+moreThanYearN).prop 'checked'
    showWrapper(moreThanYearNWrap)
  else
    hideWrapper(moreThanYearNWrap)

  if $("#"+paidMoneyY).prop 'checked'
    showWrapper(paidMoneyYWrap)
  else
    hideWrapper(paidMoneyYWrap)


showWrapper = (wrapper) ->
  $("#" + wrapper).slideDown(0).attr 'aria-hidden', 'false'

hideWrapper = (wrapper)->
  clearDownStreamInputs(wrapper)
  $("#" + wrapper).slideUp(0).attr 'aria-hidden', 'true'

clearDownStreamInputs = (wrapper)->
  $("#" + wrapper).find("input").each(clearInput)

# If we want to also clear the validation error when item is hidden ?
# $("#" + wrapper).find(".validation-error").removeClass("validation-error")
# $("#" + wrapper).find(".validation-message").remove()
clearInput = ->
  if( $(this).attr("type") == "radio" )
    $(this).prop('checked', false)
    $(this).parent().removeClass("selected")
  else
    $(this).val("")
