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
	emptyPensionExpenses = ->
    	$("#" + pensionExpenses).val("")
  $("#pensionExpenses").slideUp(0, emptyPensionExpenses).attr 'aria-hidden', 'true', ->

showHaveExpensesForJob = ->
  $("#jobExpenses").slideDown(0).attr 'aria-hidden', 'false'

hideExpensesForJob = (jobExpenses) ->
	emptyExpensesForJob = ->
    	$("#" + jobExpenses).val("")
  $("#jobExpenses").slideUp(0, emptyExpensesForJob).attr 'aria-hidden', 'true', ->