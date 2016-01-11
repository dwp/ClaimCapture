window.initEvents =(payPensionSchemeY, payPensionSchemeN, pensionExpenses,
                    haveExpensesForJobY, haveExpensesForJobN, jobExpenses) ->
  $("#" + pensionExpenses).trigger("blur")
  $("#" + jobExpenses).trigger("blur")

  if not $("#" + payPensionSchemeY).prop('checked')
    hidePensionExpenses(pensionExpenses)

  $("#" + payPensionSchemeY).on "click", ->
    showPensionExpenses(pensionExpenses)

  $("#" + payPensionSchemeN).on "click", ->
    hidePensionExpenses(pensionExpenses)

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
	emptyPensionExpenses = ->
    	$("#" + pensionExpenses).val("")
  $("#pensionExpenses").slideUp(0, emptyPensionExpenses).attr 'aria-hidden', 'true', ->

showHaveExpensesForJob = (jobExpenses) ->
  $("#" + jobExpenses).trigger("blur")
  $("#jobExpenses").slideDown(0).attr 'aria-hidden', 'false'

hideExpensesForJob = (jobExpenses) ->
	emptyExpensesForJob = ->
    	$("#" + jobExpenses).val("")
  $("#jobExpenses").slideUp(0, emptyExpensesForJob).attr 'aria-hidden', 'true', ->