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

window.payDay = (howOftenFrequency, monthlyPayDay, usuallyPaidSameAmount) ->
  $("#" + howOftenFrequency).change ->
    $("#" + usuallyPaidSameAmount).val("")
    if $("#" + howOftenFrequency).val() is "weekly"
      $("#weeklyPayDayWrap").slideDown(500)
      $("#monthlyPayDayWrap").slideUp(500)
      $("#fortnightlyPayDayWrap").slideUp(500)
      $("#otherPayDayWrap").slideUp(500)
    if $("#" + howOftenFrequency).val() is "monthly"
      $("#monthlyPayDayWrap").slideDown(500)
      $("#weeklyPayDayWrap").slideUp(500)
      $("#fortnightlyPayDayWrap").slideUp(500)
      $("#otherPayDayWrap").slideUp(500)
    else
        $("#" + monthlyPayDay).val("")
    if $("#" + howOftenFrequency).val() is "fortnightly"
      $("#fortnightlyPayDayWrap").slideDown(500)
      $("#monthlyPayDayWrap").slideUp(500)
      $("#weeklyPayDayWrap").slideUp(500)
      $("#otherPayDayWrap").slideUp(500)
    if $("#" + howOftenFrequency).val() is "other"
      $("#otherPayDayWrap").slideDown(500)
      $("#monthlyPayDayWrap").slideUp(500)
      $("#weeklyPayDayWrap").slideUp(500)
      $("#fortnightlyPayDayWrap").slideUp(500)
    if $("#" + howOftenFrequency).val() is "fourWeekly"
      $("#otherPayDayWrap").slideDown(500)
      $("#monthlyPayDayWrap").slideUp(500)
      $("#weeklyPayDayWrap").slideUp(500)
      $("#fortnightlyPayDayWrap").slideUp(500)
