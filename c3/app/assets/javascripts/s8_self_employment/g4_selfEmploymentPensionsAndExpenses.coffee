window.initEvents =(payPensionSchemeY, payPensionSchemeN, pensionExpenses,
                    haveExpensesForJobY, haveExpensesForJobN, jobExpenses) ->

  if not $("#" + payPensionSchemeY).prop('checked')
    hidePensionExpenses(pensionExpenses)

  $("#" + payPensionSchemeY).on "click", ->
    showPensionExpenses()

  $("#" + payPensionSchemeN).on "click", ->
    hidePensionExpenses(pensionExpenses)

  if not $("#" + haveExpensesForJobY).prop('checked')
    hideExpensesForJob(jobExpenses)

  $("#" + haveExpensesForJobY).on "click", ->
    showHaveExpensesForJob()

  $("#" + haveExpensesForJobN).on "click", ->
    hideExpensesForJob(jobExpenses)


showPensionExpenses = ->
  $("#pensionExpenses").slideDown(0).attr 'aria-hidden', 'false'

hidePensionExpenses = (pensionExpenses) ->
  $("#pensionExpenses").slideUp(0).attr 'aria-hidden', 'true', ->
    $("#" + pensionExpenses).val("")

showHaveExpensesForJob = ->
  $("#jobExpenses").slideDown(0).attr 'aria-hidden', 'false'

hideExpensesForJob = (jobExpenses) ->
  $("#jobExpenses").slideUp(0).attr 'aria-hidden', 'true', ->
    $("#" + jobExpenses).val("")