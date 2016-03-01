window.initEvents =(payPensionSchemeY, payPensionSchemeN, pensionExpenses,
                    payForThingsY,payForThingsN,payForThings,
                    haveExpensesForJobY, haveExpensesForJobN, jobExpenses) ->
  $("#" + pensionExpenses).trigger("blur")
  $("#" + payForThings).trigger("blur")
  $("#" + jobExpenses).trigger("blur")

  if not $("#" + payPensionSchemeY).prop('checked')
    hidePensionExpenses(pensionExpenses)

  $("#" + payPensionSchemeY).on "click", ->
    showPensionExpenses(pensionExpenses)

  $("#" + payPensionSchemeN).on "click", ->
    hidePensionExpenses(pensionExpenses)

  if not $("#" + payForThingsY).prop('checked')
    hidePayForThings(payForThings)

  $("#" + payForThingsY).on "click", ->
    showPayForThings(payForThings)

  $("#" + payForThingsN).on "click", ->
    hidePayForThings(payForThings)

  if not $("#" + haveExpensesForJobY).prop('checked')
    hideExpensesForJob(jobExpenses)

  $("#" + haveExpensesForJobY).on "click", ->
    showHaveExpensesForJob(jobExpenses)

  $("#" + haveExpensesForJobN).on "click", ->
    hideExpensesForJob(jobExpenses)


showPensionExpenses = (pensionExpenses) ->
  $("#" + pensionExpenses).trigger("blur")
  $("#pensionExpenses").slideDown(0).attr 'aria-hidden', 'false'

hidePensionExpenses = (pensionExpenses) ->
  $("#" + pensionExpenses).val("")
  $("#pensionExpenses").slideUp(0).attr 'aria-hidden', 'true', ->

showHaveExpensesForJob = (jobExpenses) ->
  $("#" + jobExpenses).trigger("blur")
  $("#jobExpenses").slideDown(0).attr 'aria-hidden', 'false'

hideExpensesForJob = (jobExpenses) ->
  $("#" + jobExpenses).val("")
  $("#jobExpenses").slideUp(0).attr 'aria-hidden', 'true', ->

showPayForThings = (payForThings) ->
  $("#" + payForThings).trigger("blur")
  $("#payForThings").slideDown(0).attr 'aria-hidden', 'false'

hidePayForThings = (payForThings) ->
  $("#" + payForThings).val("")
  $("#payForThings").slideUp(0).attr 'aria-hidden', 'true', ->