window.fixErrorMessages = (howOften,
                           howOftenFrequency,
                           usuallyPaidSameAmount,
                           usuallyPaidSameAmountText,
                           usuallyPaidSameAmountWeekText
                           usuallyPaidSameAmountFortnightText
                           usuallyPaidSameAmountFourWeekText
                           usuallyPaidSameAmountMonthText
                           usuallyPaidSameAmountOtherText) ->
    currentText = $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text().trim()
    existingError = currentText.substring(usuallyPaidSameAmountText.length, currentText.length)
    textToUse = switch ($("#" + howOftenFrequency).val())
      when "weekly" then usuallyPaidSameAmountWeekText
      when "fortnightly" then usuallyPaidSameAmountFortnightText
      when "fourweekly" then usuallyPaidSameAmountFourWeekText
      when "monthly" then usuallyPaidSameAmountMonthText
      else usuallyPaidSameAmountOtherText
    $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text(textToUse + existingError)


window.usuallyPaidSameAmount = (howOftenFrequency,
                                monthlyPayDay
                                usuallyPaidSameAmount,
                                usuallyPaidSameAmountY,
                                usuallyPaidSameAmountN,
                                defaultValue,
                                usuallyPaidSameAmountWeeklyText,
                                usuallyPaidSameAmountFortnightlyText,
                                usuallyPaidSameAmountFourWeeklyText,
                                usuallyPaidSameAmountMonthlyText,
                                usuallyPaidSameAmountOtherText) ->
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
      if ($("#" + howOftenFrequency).val() == "weekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountWeeklyText)
      if ($("#" + howOftenFrequency).val() == "fortnightly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFortnightlyText)
      if ($("#" + howOftenFrequency).val() == "fourWeekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFourWeeklyText)
      if ($("#" + howOftenFrequency).val() == "monthly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountMonthlyText)
      if ($("#" + howOftenFrequency).val() == "other")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountOtherText)
      $("#" + usuallyPaidSameAmountY).prop('checked', false)
      $("#" + usuallyPaidSameAmountN).prop('checked', false)
      $("#usuallyPaidSameAmountWrap").slideDown(500)
    if $("#" + howOftenFrequency).val() is "monthly"
      $("#monthlyPayDayWrap").slideDown(500)

window.employerOwesYouMoney = (usuallyPaidSameAmountY, usuallyPaidSameAmountN) ->
  $("#" + usuallyPaidSameAmountY).on "click", ->
    if ($("#" + usuallyPaidSameAmountY).prop('checked'))
      $("#employerOwesYouMoneyHelpWrap").slideDown 500
      $("#employerOwesYouMoneyHelpWrap").css('display', "block")
  $("#" + usuallyPaidSameAmountN).on "click", ->
    if ($("#" + usuallyPaidSameAmountY).prop('checked'))
    else
      $("#employerOwesYouMoneyHelpWrap").slideUp 500
      $("#employerOwesYouMoneyHelpWrap").css('display', "block")

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
