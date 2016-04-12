window.initEvents = (answerY, answerN, noWrap,
                     startY, startN, startYesWrap, startNoWrap,
                     haveAccountsY, haveAccountsN, haveAccountsYWrap, haveAccountsNWrap,
                     tradingYearY, tradingYearN, tradingYearYWrap,
                      paidMoneyY, paidMoneyN, paidMoneyYWrap) ->
  if not $("#"+answerN).prop 'checked'
    hideWrapper(noWrap)
  $("#" + answerY).on "click", ->
    hideWrapper(noWrap)
  $("#" + answerN).on "click", ->
    showWrapper(noWrap)

  if not $("#"+startY).prop 'checked'
    hideWrapper(startYesWrap)
  if not $("#"+startN).prop 'checked'
    hideWrapper(startNoWrap)
  $("#" + startY).on "click", ->
    showWrapper(startYesWrap)
    hideWrapper(startNoWrap)
  $("#" + startN).on "click", ->
    showWrapper(startNoWrap)
    hideWrapper(startYesWrap)

  if not $("#"+haveAccountsY).prop 'checked'
    hideWrapper(haveAccountsYWrap)
  if not $("#"+haveAccountsN).prop 'checked'
    hideWrapper(haveAccountsNWrap)
  $("#" + haveAccountsY).on "click", ->
    showWrapper(haveAccountsYWrap)
    hideWrapper(haveAccountsNWrap)
  $("#" + haveAccountsN).on "click", ->
    showWrapper(haveAccountsNWrap)
    hideWrapper(haveAccountsYWrap)

  if not $("#"+tradingYearY).prop 'checked'
    hideWrapper(tradingYearYWrap)
  $("#" + tradingYearY).on "click", ->
    showWrapper(tradingYearYWrap)
  $("#" + tradingYearN).on "click", ->
    hideWrapper(tradingYearYWrap)

  if not $("#"+paidMoneyY).prop 'checked'
    hideWrapper(paidMoneyYWrap)
  $("#" + paidMoneyY).on "click", ->
    showWrapper(paidMoneyYWrap)
  $("#" + paidMoneyN).on "click", ->
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
