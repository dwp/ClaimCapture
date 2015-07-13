window.initEvents =(payPensionSchemeY, payPensionSchemeN, pensionExpenses,
                    payForThingsY,payForThingsN,payForThings,
                    haveExpensesForJobY, haveExpensesForJobN, jobExpenses) ->

  if not $("#" + payPensionSchemeY).prop('checked')
    hidePensionExpenses(pensionExpenses)

  $("#" + payPensionSchemeY).on "click", ->
    showPensionExpenses()

  $("#" + payPensionSchemeN).on "click", ->
    hidePensionExpenses(pensionExpenses)

  if not $("#" + payForThingsY).prop('checked')
    hidePayForThings(payForThings)

  $("#" + payForThingsY).on "click", ->
    showPayForThings()

  $("#" + payForThingsN).on "click", ->
    hidePayForThings(payForThings)

  if not $("#" + haveExpensesForJobY).prop('checked')
    hideExpensesForJob(jobExpenses)

  $("#" + haveExpensesForJobY).on "click", ->
    showHaveExpensesForJob()

  $("#" + haveExpensesForJobN).on "click", ->
    hideExpensesForJob(jobExpenses)


showPensionExpenses = ->
  $("#pensionExpenses").slideDown 0

hidePensionExpenses = (pensionExpenses) ->
  $("#pensionExpenses").slideUp 0, ->
    $("#" + pensionExpenses).val("")

showHaveExpensesForJob = ->
  $("#jobExpenses").slideDown 0

hideExpensesForJob = (jobExpenses) ->
  $("#jobExpenses").slideUp 0, ->
    $("#" + jobExpenses).val("")

showPayForThings = ->
  $("#payForThings").slideDown 0

hidePayForThings = (payForThings) ->
  $("#payForThings").slideUp 0, ->
    $("#" + payForThings).val("")