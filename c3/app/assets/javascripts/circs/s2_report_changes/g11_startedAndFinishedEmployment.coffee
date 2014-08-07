window.fixErrorMessages = (beenPaidYet, beenPaidYetY, beenPaidYetN, beenPaidYetOldText, beenPaidYetText,
                           howMuchPaid, howMuchPaidOldText, howMuchPaidYText, howMuchPaidNText,
                           dateLastPaid, dateLastPaidYText, dateLastPaidNText,
                           whatWasIncluded, whatWasIncludedYText, whatWasIncludedNText,
                           howOften, howOftenFrequency,
                           howOftenText, howOftenCircsText,
                           usuallyPaidSameAmount, usuallyPaidSameAmountText,
                           usuallyPaidSameAmountWeekText, usuallyPaidSameAmountFortnightText,
                           usuallyPaidSameAmountFourWeekText, usuallyPaidSameAmountMonthText, usuallyPaidSameAmountOtherText,
                           doYouPayIntoPensionAnswer, doYouPayIntoPensionText, didYouPayIntoPensionText,
                           doCareCostsForThisWorkAnswer, doCareCostsForThisWorkText, didCareCostsForThisWorkText) ->
  beenPaidYetError = $("div[class='validation-summary'] a[href='#" + beenPaidYet + "']")
  currentText = if beenPaidYetError.length > 0 then beenPaidYetError.text().trim() else ""
  existingError = currentText.substring(beenPaidYetOldText.length, currentText.length)
  beenPaidYetError.text(beenPaidYetText + existingError)
  howMuchPaidError = $("div[class='validation-summary'] a[href='#" + howMuchPaid + "']")
  currentText = if howMuchPaidError.length > 0 then howMuchPaidError.text().trim() else ""
  existingError = currentText.substring(howMuchPaidOldText.length, currentText.length)
  howMuchPaidError.text(howMuchPaidYText + existingError)

  if ($("#" + beenPaidYetN).prop('checked'))
    currentText = if howMuchPaidError.length > 0 then howMuchPaidError.text().trim() else ""
    existingError = currentText.substring(howMuchPaidOldText.length, currentText.length)
    howMuchPaidError.text(howMuchPaidNText + existingError)
    dateLastPaidError = $("div[class='validation-summary'] a[href='#" + dateLastPaid + "']")
    currentText = if dateLastPaidError.length > 0 then dateLastPaidError.text().trim() else ""
    existingError = currentText.substring(dateLastPaidYText.length, currentText.length)
    $("div[class='validation-summary'] a[href='#" + dateLastPaid + "']").text(dateLastPaidNText + existingError)

  if ($("#" + beenPaidYetY).prop('checked'))
    currentText = if howMuchPaidError.length > 0 then howMuchPaidError.text().trim() else ""
    existingError = currentText.substring(howMuchPaidOldText.length, currentText.length)
    howMuchPaidError.text(howMuchPaidYText + existingError)

  pathUsuallyPaidSameAmount = $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']")
  if ((pathUsuallyPaidSameAmount != undefined) && (pathUsuallyPaidSameAmount.length > 0))
    currentTextUsuallyPaidSameAmount = pathUsuallyPaidSameAmount.text().trim()
    existingErrorUsuallyPaidSameAmount = currentTextUsuallyPaidSameAmount.substring(usuallyPaidSameAmountText.length, currentTextUsuallyPaidSameAmount.length)
    textToUseUsuallyPaidSameAmount = switch ($("#" + howOftenFrequency).val())
      when "Weekly" then usuallyPaidSameAmountWeekText
      when "Fortnightly" then usuallyPaidSameAmountFortnightText
      when "Four-Weekly" then usuallyPaidSameAmountFourWeekText
      when "Monthly" then usuallyPaidSameAmountMonthText
      else usuallyPaidSameAmountOtherText
    pathUsuallyPaidSameAmount.text(textToUseUsuallyPaidSameAmount + existingErrorUsuallyPaidSameAmount)

  pathHowOften = $("div[class='validation-summary'] a[href='#" + howOften + "']")
  if pathHowOften != undefined and pathHowOften.length > 0
    currentTextHowOften = pathHowOften.text().toString()
    currentTextHowOften = if currentTextHowOften.trim? then currentTextHowOften.trim() else currentTextHowOften
    existingErrorHowOften = currentTextHowOften.substring(howOftenText.length, currentTextHowOften.length)
    pathHowOften.text(howOftenCircsText + existingErrorHowOften)

  pathDoYouPayIntoPensionAnswer = $("div[class='validation-summary'] a[href='#" + doYouPayIntoPensionAnswer + "']")
  if pathDoYouPayIntoPensionAnswer != undefined and pathDoYouPayIntoPensionAnswer.length > 0
    currentTextDoYouPayIntoPensionAnswer = pathDoYouPayIntoPensionAnswer.text().trim()
    existingErrorDoYouPayIntoPensionAnswer = currentTextDoYouPayIntoPensionAnswer.substring(doYouPayIntoPensionText.length, currentTextDoYouPayIntoPensionAnswer.length)
    pathDoYouPayIntoPensionAnswer.text(didYouPayIntoPensionText + existingErrorDoYouPayIntoPensionAnswer)

  pathDoCareCostsForThisWorkAnswer = $("div[class='validation-summary'] a[href='#" + doCareCostsForThisWorkAnswer + "']")
  if pathDoCareCostsForThisWorkAnswer != undefined and pathDoCareCostsForThisWorkAnswer.length > 0
    currentTextDoCareCostsForThisWorkAnswer = pathDoCareCostsForThisWorkAnswer.text().trim()
    existingErrorDoCareCostsForThisWorkAnswer = currentTextDoCareCostsForThisWorkAnswer.substring(doCareCostsForThisWorkText.length, currentTextDoCareCostsForThisWorkAnswer.length)
    pathDoCareCostsForThisWorkAnswer.text(didCareCostsForThisWorkText + existingErrorDoCareCostsForThisWorkAnswer)

window.beenPaidYet = (beenPaidYetY, beenPaidYetN,
                      howMuchPaid, howMuchPaidYText, howMuchPaidNText,
                      dateLastPaid, dateLastPaidDay, dateLastPaidMonth, dateLastPaidYear, dateLastPaidYText, dateLastPaidNText,
                      whatWasIncluded, whatWasIncludedYText, whatWasIncludedNText) ->
  $("#" + beenPaidYetY).on "click", ->
    $("#beenPaidYetWrap").slideUp 500, ->
      $("#beenPaidYetWrap").css('display', "none")
      $("label[for='" + howMuchPaid + "']").text(howMuchPaidYText)
      $("#" + howMuchPaid).val("")
      $("#" + dateLastPaid).parent().find("legend").html(dateLastPaidYText)
      $("#" + dateLastPaidDay).val("")
      $("#" + dateLastPaidMonth).val("")
      $("#" + dateLastPaidYear).val("")
      $("#" + dateLastPaid + "_defaultDateContextualHelp").css('display', "block")
      $("#" + dateLastPaid + "_alternativeDateContextualHelp").css('display', "none")
      $("label[for='" + whatWasIncluded + "']").text(whatWasIncludedYText)
      $("#" + whatWasIncluded).val("")
    $("#beenPaidYetWrap").slideDown 500, ->
      $("#beenPaidYetWrap").css('display', "block")

  $("#" + beenPaidYetN).on "click", ->
    $("#beenPaidYetWrap").slideUp 500, ->
      $("#beenPaidYetWrap").css('display', "none")
      $("label[for='" + howMuchPaid + "']").text(howMuchPaidNText)
      $("#" + howMuchPaid).val("")
      $("#" + dateLastPaid).parent().find("legend").html(dateLastPaidNText)
      $("#" + dateLastPaidDay).val("")
      $("#" + dateLastPaidMonth).val("")
      $("#" + dateLastPaidYear).val("")
      $("#" + dateLastPaid + "_defaultDateContextualHelp").css('display', "none")
      $("#" + dateLastPaid + "_alternativeDateContextualHelp").css('display', "block")
      $("label[for='" + whatWasIncluded + "']").text(whatWasIncludedNText)
      $("#" + whatWasIncluded).val("")
    $("#beenPaidYetWrap").slideDown 500, ->
      $("#beenPaidYetWrap").css('display', "block")

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
      if ($("#" + howOftenFrequency).val() == "Weekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountWeeklyText)
      if ($("#" + howOftenFrequency).val() == "Fortnightly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFortnightlyText)
      if ($("#" + howOftenFrequency).val() == "Four-Weekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFourWeeklyText)
      if ($("#" + howOftenFrequency).val() == "Monthly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountMonthlyText)
      if ($("#" + howOftenFrequency).val() == "Other")
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
      $("#employerOwesYouMoneyHelpWrap").slideUp 500, ->
        $("#employerOwesYouMoneyHelpWrap").css('display', "none")

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
