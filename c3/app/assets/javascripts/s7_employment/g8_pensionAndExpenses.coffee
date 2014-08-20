window.initEvents =(payPensionSchemeY, payPensionSchemeN, pensionExpenses,
                    haveExpensesForJobY, haveExpensesForJobN, jobExpenses) ->

  $("#" + payPensionSchemeY).on "click", ->
    $("#pensionExpenses").slideDown 500
    $("#pensionExpenses").css('display', "block")

  $("#" + payPensionSchemeN).on "click", ->
    $("#pensionExpenses").slideUp 500, ->
      $("#" + pensionExpenses).val("")

  $("#" + haveExpensesForJobY).on "click", ->
    $("#jobExpenses").slideDown 500
    $("#jobExpenses").css('display', "block")

  $("#" + haveExpensesForJobN).on "click", ->
    $("#jobExpenses").slideUp 500, ->
      $("#" + jobExpenses).val("")