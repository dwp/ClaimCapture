window.initStatutorySickPayEvents = (stillBeingPaidStatutorySickPay_yes,
                                    stillBeingPaidStatutorySickPay_no,
                                    whenDidYouLastGetPaid_day,
                                    whenDidYouLastGetPaid_month,
                                    whenDidYouLastGetPaid_year,
                                    howOftenPaidStatutorySickPay_Other,
                                    howOftenPaidStatutorySickPay,
                                    howOftenPaidStatutorySickPayValue,
                                    howOftenPaidStatutorySickPayOther) ->
  if not $("#" + stillBeingPaidStatutorySickPay_no).prop('checked')
    hideStillBeingPaidStatutorySickPayWrap(whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year)

  if $("#" + stillBeingPaidStatutorySickPay_no).prop('checked')
    showStillBeingPaidStatutorySickPayWrap()

  $("#" + stillBeingPaidStatutorySickPay_no).on "click", ->
    showStillBeingPaidStatutorySickPayWrap()

  $("#" + stillBeingPaidStatutorySickPay_yes).on "click", ->
    hideStillBeingPaidStatutorySickPayWrap(whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year)

  if not $("#" + howOftenPaidStatutorySickPay_Other).prop('checked')
    hideHowOftenPaidStatutorySickPayWrap(howOftenPaidStatutorySickPayOther)

  if $("#" + howOftenPaidStatutorySickPay_Other).prop('checked')
    showHowOftenPaidStatutorySickPayWrap()

  $("#" + howOftenPaidStatutorySickPay + " input").on "change", ->
    if $(this).val() == howOftenPaidStatutorySickPayValue
      showHowOftenPaidStatutorySickPayWrap()
    else
      hideHowOftenPaidStatutorySickPayWrap(howOftenPaidStatutorySickPayOther)

hideStillBeingPaidStatutorySickPayWrap = (whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year) ->
  emptyWhenDidYouLastGetPaid = ->
    $("#" + whenDidYouLastGetPaid_day).val("")
    $("#" + whenDidYouLastGetPaid_month).val("")
    $("#" + whenDidYouLastGetPaid_year).val("")

  $("#stillBeingPaidStatutorySickPayWrap").slideUp(0, emptyWhenDidYouLastGetPaid).attr 'aria-hidden', 'true'

showStillBeingPaidStatutorySickPayWrap = ->
  $("#stillBeingPaidStatutorySickPayWrap").slideDown(0).attr 'aria-hidden', 'false'

hideHowOftenPaidStatutorySickPayWrap = (howOftenPaidStatutorySickPayOther) ->
  emptyHowOftenPaidStatutorySickPay = ->
    $("#" + howOftenPaidStatutorySickPayOther).val("")

  $("#howOftenPaidStatutorySickPayWrap").slideUp(0, emptyHowOftenPaidStatutorySickPay).attr 'aria-hidden', 'true'

showHowOftenPaidStatutorySickPayWrap = ->
  $("#howOftenPaidStatutorySickPayWrap").slideDown(0).attr 'aria-hidden', 'false'
