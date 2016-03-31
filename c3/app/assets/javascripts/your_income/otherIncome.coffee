window.initStatutorySickPayEvents = (stillBeingPaidThisPay_yes,
                                    stillBeingPaidThisPay_no,
                                    whenDidYouLastGetPaid_day,
                                    whenDidYouLastGetPaid_month,
                                    whenDidYouLastGetPaid_year,
                                    howOftenPaidThisPay_Other,
                                    howOftenPaidThisPay,
                                    howOftenPaidThisPayValue,
                                    howOftenPaidThisPayOther) ->
  if not $("#" + stillBeingPaidThisPay_no).prop('checked')
    hidestillBeingPaidThisPayWrap(whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year)

  if $("#" + stillBeingPaidThisPay_no).prop('checked')
    showstillBeingPaidThisPayWrap()

  $("#" + stillBeingPaidThisPay_no).on "click", ->
    showstillBeingPaidThisPayWrap()

  $("#" + stillBeingPaidThisPay_yes).on "click", ->
    hidestillBeingPaidThisPayWrap(whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year)

  if not $("#" + howOftenPaidThisPay_Other).prop('checked')
    hidehowOftenPaidThisPayWrap(howOftenPaidThisPayOther)

  if $("#" + howOftenPaidThisPay_Other).prop('checked')
    showhowOftenPaidThisPayWrap()

  $("#" + howOftenPaidThisPay + " input").on "change", ->
    if $(this).val() == howOftenPaidThisPayValue
      showhowOftenPaidThisPayWrap()
    else
      hidehowOftenPaidThisPayWrap(howOftenPaidThisPayOther)

hidestillBeingPaidThisPayWrap = (whenDidYouLastGetPaid_day, whenDidYouLastGetPaid_month, whenDidYouLastGetPaid_year) ->
  emptyWhenDidYouLastGetPaid = ->
    $("#" + whenDidYouLastGetPaid_day).val("")
    $("#" + whenDidYouLastGetPaid_month).val("")
    $("#" + whenDidYouLastGetPaid_year).val("")

  $("#stillBeingPaidThisPayWrap").slideUp(0, emptyWhenDidYouLastGetPaid).attr 'aria-hidden', 'true'

showstillBeingPaidThisPayWrap = ->
  $("#stillBeingPaidThisPayWrap").slideDown(0).attr 'aria-hidden', 'false'

hidehowOftenPaidThisPayWrap = (howOftenPaidThisPayOther) ->
  emptyhowOftenPaidThisPay = ->
    $("#" + howOftenPaidThisPayOther).val("")

  $("#howOftenPaidThisPayWrap").slideUp(0, emptyhowOftenPaidThisPay).attr 'aria-hidden', 'true'

showhowOftenPaidThisPayWrap = ->
  $("#howOftenPaidThisPayWrap").slideDown(0).attr 'aria-hidden', 'false'
