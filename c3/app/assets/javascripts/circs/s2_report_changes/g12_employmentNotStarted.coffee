window.fixErrorMessages = (beenPaidYet,
                           beenPaidYetText,
                           willBePaidText,
                           howOften,
                           howOftenText,
                           howOftenWillText
                           howOftenFrequency,
                           usuallyPaidSameAmount,
                           usuallyPaidSameAmountText,
                           usuallyPaidSameAmountWeekText,
                           usuallyPaidSameAmountFortnightText,
                           usuallyPaidSameAmountFourWeekText,
                           usuallyPaidSameAmountMonthText,
                           usuallyPaidSameAmountOtherText,
                           doYouPayIntoPensionAnswer,
                           doYouPayIntoPensionText,
                           willYouPayIntoPensionText,
                           doCareCostsForThisWorkAnswer,
                           doCareCostsForThisWorkText,
                           willCareCostsForThisWorkText) ->
  pathBeenPaidYet = $("div[class='validation-summary'] a[href='#" + beenPaidYet + "']")
  if ((pathBeenPaidYet != undefined) && (pathBeenPaidYet.length > 0))
    currentTextBeenPaidYet = pathBeenPaidYet.text().trim()
    existingErrorBeenPaidYet = currentTextBeenPaidYet.substring(beenPaidYetText.length, currentTextBeenPaidYet.length)
    pathBeenPaidYet.text(willBePaidText + existingErrorBeenPaidYet)
  currentText = $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text().trim()
  existingError = currentText.substring(usuallyPaidSameAmountText.length, currentText.length)
  textToUse = switch ($("#" + howOftenFrequency).val())
    when "weekly" then usuallyPaidSameAmountWeekText
    when "fortnightly" then usuallyPaidSameAmountFortnightText
    when "fourweekly" then usuallyPaidSameAmountFourWeekText
    when "monthly" then usuallyPaidSameAmountMonthText
    else usuallyPaidSameAmountOtherText
  $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text(textToUse + existingError)
  pathDoYouPayIntoPensionAnswer = $("div[class='validation-summary'] a[href='#" + doYouPayIntoPensionAnswer + "']")
  if ((pathDoYouPayIntoPensionAnswer != undefined) && (pathDoYouPayIntoPensionAnswer.length > 0))
    currentTextDoYouPayIntoPensionAnswer = pathDoYouPayIntoPensionAnswer.text().trim()
    existingErrorDoYouPayIntoPensionAnswer = currentTextDoYouPayIntoPensionAnswer.substring(doYouPayIntoPensionText.length, currentTextDoYouPayIntoPensionAnswer.length)
    pathDoYouPayIntoPensionAnswer.text(willYouPayIntoPensionText + existingErrorDoYouPayIntoPensionAnswer)
  pathDoCareCostsForThisWorkAnswer = $("div[class='validation-summary'] a[href='#" + doCareCostsForThisWorkAnswer + "']")
  if ((pathDoCareCostsForThisWorkAnswer != undefined) && (pathDoCareCostsForThisWorkAnswer.length > 0))
    currentTextDoCareCostsForThisWorkAnswer = pathDoCareCostsForThisWorkAnswer.text().trim()
    existingErrorDoCareCostsForThisWorkAnswer = currentTextDoCareCostsForThisWorkAnswer.substring(doCareCostsForThisWorkText.length, currentTextDoCareCostsForThisWorkAnswer.length)
    pathDoCareCostsForThisWorkAnswer.text(willCareCostsForThisWorkText + existingErrorDoCareCostsForThisWorkAnswer)
  pathHowOften = $("div[class='validation-summary'] a[href='#" + howOften + "']")
  if ((pathHowOften != undefined) && (pathHowOften.length > 0))
    currentTextHowOften = pathHowOften.text().trim()
    existingErrorHowOften = currentTextHowOften.substring(howOftenText.length, currentTextHowOften.length)
    pathHowOften.text(howOftenWillText + existingErrorHowOften)


window.beenPaidYet = (beenPaidYetY, beenPaidYetN) ->
  $("#" + beenPaidYetY).on "click", ->
    $("#beenPaidYetWrap").slideDown 500
    $("#beenPaidYetWrap").css('display', "block")

  $("#" + beenPaidYetN).on "click", ->
    $("#beenPaidYetWrap").slideUp 500

window.usuallyPaidSameAmount = (howOftenFrequency,
                                usuallyPaidSameAmount,
                                usuallyPaidSameAmountY,
                                usuallyPaidSameAmountN,
                                defaultValue,
                                dontknowKey,
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
    if (($("#" + howOftenFrequency).val() is defaultValue) or ($("#" + howOftenFrequency).val() is "") or ($("#" + howOftenFrequency).val() is dontknowKey))
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
