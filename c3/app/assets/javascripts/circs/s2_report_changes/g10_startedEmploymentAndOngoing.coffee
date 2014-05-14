window.beenPaidYet = (beenPaidYetY, beenPaidYetN,
                      howMuchPaid, howMuchPaidYText, howMuchPaidNText,
                      whatDatePaid, whatDatePaidDay, whatDatePaidMonth, whatDatePaidYear, whatDatePaidYText, whatDatePaidNText,
                      howOften, howOftenFrequencyOther, howOftenYText, howOftenNText,
                      monthlyPayDay, monthlyPayDayYText, monthlyPayDayNText,
                      usuallyPaidSameAmount, usuallyPaidSameAmountY, usuallyPaidSameAmountN, usuallyPaidSameAmountYText, usuallyPaidSameAmountNText) ->
  $("#" + beenPaidYetY).on "click", ->
    $("#beenPaidYetWrap").slideUp 500, ->
      $("#beenPaidYetWrap").css('display', "none")
      $("label[for='" + howMuchPaid + "']").text(howMuchPaidYText)
      $("#" + howMuchPaid).val("")
      $("label[for='" + whatDatePaid + "']").text(whatDatePaidYText)
      $("#" + whatDatePaidDay).val("")
      $("#" + whatDatePaidMonth).val("")
      $("#" + whatDatePaidYear).val("")
      $("label[for='" + howOften + "']").text(howOftenYText)
      $("#" + howOften).val("")
      $("label[for='" + monthlyPayDay + "']").text(monthlyPayDayYText)
      $("#" + monthlyPayDay).val("")
      $("label[for='" + usuallyPaidSameAmount + "']").text(usuallyPaidSameAmountYText)
      $("#" + usuallyPaidSameAmountY).prop('checked', false)
      $("#" + usuallyPaidSameAmountN).prop('checked', false)
    $("#usuallyPaidSameAmountWrap").slideUp 500, ->
      $("#usuallyPaidSameAmountWrap").css('display', "none")
    if ($("#monthlyPayDayWrap").is(":visible"))
      $("#monthlyPayDayWrap").slideUp 500, ->
        $("#monthlyPayDayWrap").css('display', "none")
    $("#beenPaidYetWrap").slideDown 500, ->
      $("#beenPaidYetWrap").css('display', "block")
    if ($("#howOften_wrap").is(":visible"))
      $("#howOften_wrap").slideUp 500, ->
        $("#howOften_wrap").css('display', "none")
        $("#" + howOftenFrequencyOther).val("")

  $("#" + beenPaidYetN).on "click", ->
    $("#beenPaidYetWrap").slideUp 500, ->
      $("#beenPaidYetWrap").css('display', "none")
      $("label[for='" + howMuchPaid + "']").text(howMuchPaidNText)
      $("#" + howMuchPaid).val("")
      $("label[for='" + whatDatePaid + "']").text(whatDatePaidNText)
      $("#" + whatDatePaidDay).val("")
      $("#" + whatDatePaidMonth).val("")
      $("#" + whatDatePaidYear).val("")
      $("label[for='" + howOften + "']").text(howOftenNText)
      $("#" + howOften).val("")
      $("label[for='" + monthlyPayDay + "']").text(monthlyPayDayNText)
      $("#" + monthlyPayDay).val("")
      $("label[for='" + usuallyPaidSameAmount + "']").text(usuallyPaidSameAmountNText)
      $("#" + usuallyPaidSameAmountY).prop('checked', false)
      $("#" + usuallyPaidSameAmountN).prop('checked', false)
    $("#usuallyPaidSameAmountWrap").slideUp 500, ->
      $("#usuallyPaidSameAmountWrap").css('display', "none")
    if ($("#monthlyPayDayWrap").is(":visible"))
      $("#monthlyPayDayWrap").slideUp 500, ->
        $("#monthlyPayDayWrap").css('display', "none")
    $("#beenPaidYetWrap").slideDown 500, ->
      $("#beenPaidYetWrap").css('display', "block")
    if ($("#howOften_wrap").is(":visible"))
      $("#howOften_wrap").slideUp 500, ->
        $("#howOften_wrap").css('display', "none")
        $("#" + howOftenFrequencyOther).val("")

window.payDay = (howOftenFrequency, monthlyPayDay, usuallyPaidSameAmount, usuallyPaidSameAmountY, usuallyPaidSameAmountN, defaultValue) ->
  $("#" + howOftenFrequency).change ->
    if ($("#usuallyPaidSameAmountWrap").is(":visible"))
      $("#usuallyPaidSameAmountWrap").slideUp 500, ->
        $("#usuallyPaidSameAmountWrap").css('display', "none")
        $("#" + usuallyPaidSameAmountY).prop('checked', false)
        $("#" + usuallyPaidSameAmountN).prop('checked', false)
    if ($("#monthlyPayDayWrap").is(":visible"))
      $("#monthlyPayDayWrap").slideUp 500, ->
        $("#monthlyPayDayWrap").css('display', "none")
        $("#" + monthlyPayDay).val("")

    if (($("#" + howOftenFrequency).val() is defaultValue) or ($("#" + howOftenFrequency).val() is ""))
    else
      $("#usuallyPaidSameAmountWrap").slideDown(500)

    if $("#" + howOftenFrequency).val() is "monthly"
      $("#monthlyPayDayWrap").slideDown(500)

window.whatFor = (payIntoPensionY, payIntoPensionN, whatFor) ->
  $("#" + payIntoPensionY).on "click", ->
    $("#whatForWrap").slideDown 500
    $("#whatForWrap").css('display', "block")

  $("#" + payIntoPensionN).on "click", ->
    $("#whatForWrap").slideUp 500, ->
      $("#" + whatFor).val("")

window.whatCosts = (careCostsForThisWorkY, careCostsForThisWorkN, whatCosts) ->
  $("#" + careCostsForThisWorkY).on "click", ->
    $("#whatCostsWrap").slideDown 500
    $("#whatCostsWrap").css('display', "block")

  $("#" + careCostsForThisWorkN).on "click", ->
    $("#whatCostsWrap").slideUp 500, ->
      $("#" + whatCosts).val("")
