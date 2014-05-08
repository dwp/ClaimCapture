window.haventBeenPaidYet = (haventBeenPaidYet) ->
  $("#" + haventBeenPaidYet).on "click", ->
    if $("#" + haventBeenPaidYet).is(":checked")
      $("#haventBeenPaidYetWrap").slideDown 500
      $("#haventBeenPaidYetWrap").css('display', "block")
      $("#whatDatePaidWrap").slideDown 500
      $("#whatDatePaidWrap").css('display', "block")
    else
      $("#haventBeenPaidYetWrap").slideUp 500
      $("#whatDatePaidWrap").slideUp 500

window.monthlyPayDay = (howOftenFrequency, monthlyPayDay) ->
  $("#" + howOftenFrequency).change ->
    if $("#" + howOftenFrequency).val() is "monthly"
      $("#monthlyPayDayWrap").slideDown()
    else
      $("#monthlyPayDayWrap").slideUp ->
        $("#" + monthlyPayDay).val("")
