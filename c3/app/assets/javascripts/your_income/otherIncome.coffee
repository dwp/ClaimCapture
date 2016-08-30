window.initEvents = (stillBeingPaidThisPay_yes,
                      stillBeingPaidThisPay_no,
                      whenDidYouLastGetPaid_day,
                      whenDidYouLastGetPaid_month,
                      whenDidYouLastGetPaid_year,
                      howOftenPaidThisPay_Other,
                      howOftenPaidThisPay,
                      howOftenPaidThisPayValue,
                      howOftenPaidThisPayOther) ->
  if not $("#" + stillBeingPaidThisPay_no).prop('checked')
    hideStillBeingPaidThisPayWrap(whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year)

  if $("#" + stillBeingPaidThisPay_no).prop('checked')
    showStillBeingPaidThisPayWrap()

  $("#" + stillBeingPaidThisPay_no).on "click", ->
    showStillBeingPaidThisPayWrap()

  $("#" + stillBeingPaidThisPay_yes).on "click", ->
    hideStillBeingPaidThisPayWrap(whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year)

  if not $("#" + howOftenPaidThisPay_Other).prop('checked')
    hideHowOftenPaidThisPayWrap(howOftenPaidThisPayOther)

  if $("#" + howOftenPaidThisPay_Other).prop('checked')
    showHowOftenPaidThisPayWrap()

  $("#" + howOftenPaidThisPay + " input").on "change", ->
    if $(this).val() == howOftenPaidThisPayValue
      showHowOftenPaidThisPayWrap()
    else
      hideHowOftenPaidThisPayWrap(howOftenPaidThisPayOther)

hideStillBeingPaidThisPayWrap = (whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year) ->
  emptyWhenDidYouLastGetPaid = ->
    $("#" + whenDidYouLastGetPaid_day).val("")
    $("#" + whenDidYouLastGetPaid_month).val("")
    $("#" + whenDidYouLastGetPaid_year).val("")

  $("#stillBeingPaidThisPayWrap").slideUp(0, emptyWhenDidYouLastGetPaid).attr 'aria-hidden', 'true'

showStillBeingPaidThisPayWrap = ->
  $("#stillBeingPaidThisPayWrap").slideDown(0).attr 'aria-hidden', 'false'

hideHowOftenPaidThisPayWrap = (howOftenPaidThisPayOther) ->
  emptyHowOftenPaidThisPay = ->
    $("#" + howOftenPaidThisPayOther).val("")

  $("#howOftenPaidThisPayWrap").slideUp(0, emptyHowOftenPaidThisPay).attr 'aria-hidden', 'true'

showHowOftenPaidThisPayWrap = ->
  $("#howOftenPaidThisPayWrap").slideDown(0).attr 'aria-hidden', 'false'

window.initFosteringAllowanceEvents = (fosteringAllowancePay, fosteringAllowancePay_Other, fosteringAllowancePayValue, fosteringAllowancePayOther) ->
  if not $("#" + fosteringAllowancePay_Other).prop('checked')
    hideFosteringAllowancePayWrap(fosteringAllowancePayOther)

  if $("#" + fosteringAllowancePay_Other).prop('checked')
    showFosteringAllowancePayWrap()

  $("#" + fosteringAllowancePay + " input").on "change", ->
    if $(this).val() == fosteringAllowancePayValue
      showFosteringAllowancePayWrap()
    else
      hideFosteringAllowancePayWrap(fosteringAllowancePayOther)

hideFosteringAllowancePayWrap = (fosteringAllowancePayOther) ->
  emptyFosteringAllowancePay = ->
    $("#" + fosteringAllowancePayOther).val("")

  $("#fosteringAllowancePayWrap").slideUp(0, emptyFosteringAllowancePay).attr 'aria-hidden', 'true'

showFosteringAllowancePayWrap = ->
  $("#fosteringAllowancePayWrap").slideDown(0).attr 'aria-hidden', 'false'

window.initOtherPaymentsEvents = (textArea) ->
  $("#" + textArea).trigger("blur")

window.initRentalIncomesEvents = (textArea) ->
  $("#" + textArea).trigger("blur")