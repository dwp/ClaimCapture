#Util functions so the code looks cleaner

#vss stands for validation-summary selector
vss = (href) -> $("div[class='validation-summary'] a[href='##{href}']")
vssText = (href,text)     -> if text? then vss(href).text(text) else if vss(href).length > 0 then vss(href).text().trim() else ""
checked = (selector,val)  -> if val?  then $("##{selector}").prop('checked',val) else $("##{selector}").prop('checked')
val = (selector,text)     -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
subString = (str,start,end) -> str.substring(start.length,end.length)
visible = (selector) -> $("##{selector}").is(":visible")
legend = (selector)-> $("##{selector}").parent().find("legend")
label = (selector) -> $("label[for='#{selector}']")
exists = (elem) -> elem? and elem.length > 0
S = (selector) -> $("##{selector}")

window.fixErrorMessages = (o) ->
  currentText = vssText(o.beenPaidYet)
  existingError = subString(currentText,o.beenPaidYetOldText,currentText)
  vss(o.beenPaidYet).text(o.beenPaidYetText + existingError)
  currentText = vssText(o.howMuchPaid)
  existingError = subString(currentText,o.howMuchPaidOldText,currentText)
  vss(o.howMuchPaid).text(o.howMuchPaidYText + existingError)

  if checked o.beenPaidYetN
    currentText = vssText(o.howMuchPaid)
    existingError = subString(currentText,o.howMuchPaidOldText,currentText)
    vss(o.howMuchPaid).text(o.howMuchPaidNText + existingError)
    currentText = vssText(o.dateLastPaid)
    existingError = subString(currentText,o.dateLastPaidYText,currentText)
    vss(o.dateLastPaid).text(o.dateLastPaidNText + existingError)

  if checked o.beenPaidYetY
    currentText = vssText(o.howMuchPaid)
    existingError = subString(currentText,o.howMuchPaidOldText,currentText)
    vss(o.howMuchPaid).text(o.howMuchPaidYText + existingError)

  pathUsuallyPaidSameAmount = vss(o.usuallyPaidSameAmount)

  if exists pathUsuallyPaidSameAmount
    currentTextUsuallyPaidSameAmount = pathUsuallyPaidSameAmount.text().trim()
    existingErrorUsuallyPaidSameAmount = subString(currentTextUsuallyPaidSameAmount,o.usuallyPaidSameAmountText,currentTextUsuallyPaidSameAmount)
    textToUseUsuallyPaidSameAmount = switch (val o.howOftenFrequency)
      when "Weekly" then o.usuallyPaidSameAmountWeekText
      when "Fortnightly" then o.usuallyPaidSameAmountFortnightText
      when "Four-Weekly" then o.usuallyPaidSameAmountFourWeekText
      when "Monthly" then o.usuallyPaidSameAmountMonthText
      else o.usuallyPaidSameAmountOtherText
    pathUsuallyPaidSameAmount.text(textToUseUsuallyPaidSameAmount + existingErrorUsuallyPaidSameAmount)
  pathHowOften = vss(o.howOften)

  if exists pathHowOften
    currentTextHowOften = pathHowOften.text().trim()
    existingErrorHowOften = subString(currentTextHowOften,o.howOftenText,currentTextHowOften)
    pathHowOften.text(o.howOftenCircsText + existingErrorHowOften)

  pathDoYouPayIntoPensionAnswer = vss(o.doYouPayIntoPensionAnswer)
  if exists pathDoYouPayIntoPensionAnswer
    currentTextDoYouPayIntoPensionAnswer = pathDoYouPayIntoPensionAnswer.text().trim()
    existingErrorDoYouPayIntoPensionAnswer = subString(currentTextDoYouPayIntoPensionAnswer,o.doYouPayIntoPensionText,currentTextDoYouPayIntoPensionAnswer)
    pathDoYouPayIntoPensionAnswer.text(o.didYouPayIntoPensionText + existingErrorDoYouPayIntoPensionAnswer)

  pathDoCareCostsForThisWorkAnswer = vss(o.doCareCostsForThisWorkAnswer)
  if exists pathDoCareCostsForThisWorkAnswer
    currentTextDoCareCostsForThisWorkAnswer = pathDoCareCostsForThisWorkAnswer.text().trim()
    existingErrorDoCareCostsForThisWorkAnswer = subString(currentTextDoCareCostsForThisWorkAnswer,o.doCareCostsForThisWorkText,currentTextDoCareCostsForThisWorkAnswer)
    pathDoCareCostsForThisWorkAnswer.text(o.didCareCostsForThisWorkText + existingErrorDoCareCostsForThisWorkAnswer)

onBeenPaidYetY = (ctx,reset) ->
  ->
    S("beenPaidYetWrap").slideUp 0, ->
      label(ctx.howMuchPaid).text(ctx.howMuchPaidYText)
      S(ctx.howMuchPaid).val("") if reset
      legend(ctx.dateLastPaid).html(ctx.dateLastPaidYText)
      S(ctx.dateLastPaidDay).val("") if reset
      S(ctx.dateLastPaidMonth).val("") if reset
      S(ctx.dateLastPaidYear).val("") if reset
      S(ctx.dateLastPaid + "_defaultDateContextualHelp").css('display', "block")
      S(ctx.dateLastPaid + "_alternativeDateContextualHelp").css('display', "none")
      label(ctx.whatWasIncluded).text(ctx.whatWasIncludedYText)
      S(ctx.whatWasIncluded).val("") if reset
    S("beenPaidYetWrap").slideDown 0

onBeenPaidYetN = (ctx,reset) ->
  ->
    S("beenPaidYetWrap").slideUp 0, ->
      label(ctx.howMuchPaid).text(ctx.howMuchPaidNText)
      S(ctx.howMuchPaid).val("") if reset
      legend(ctx.dateLastPaid).html(ctx.dateLastPaidNText)
      S(ctx.dateLastPaidDay).val("") if reset
      S(ctx.dateLastPaidMonth).val("") if reset
      S(ctx.dateLastPaidYear).val("") if reset
      S(ctx.dateLastPaid + "_defaultDateContextualHelp").css('display', "none")
      S(ctx.dateLastPaid + "_alternativeDateContextualHelp").css('display', "block")
      label(ctx.whatWasIncluded).text(ctx.whatWasIncludedNText)
      S(ctx.whatWasIncluded).val("") if reset
    S("beenPaidYetWrap").slideDown 0

window.beenPaidYet = (o) ->
  if checked o.beenPaidYetY
    onBeenPaidYetY(o,false)()
  else if checked o.beenPaidYetN
    onBeenPaidYetN(o,false)()
  else
    S("beenPaidYetWrap").slideUp 0


  S(o.beenPaidYetY).on "click", onBeenPaidYetY(o,true)
  S(o.beenPaidYetN).on "click", onBeenPaidYetN(o,true)

onUsuallyPaidSameAmount = (o) ->
  ->
    if visible("usuallyPaidSameAmountWrap")
      S("usuallyPaidSameAmountWrap").slideUp 0, ->
        checked o.usuallyPaidSameAmountY,  false
        checked o.usuallyPaidSameAmountN,  false

    S("monthlyPayDayWrap").slideUp 0, -> S(o.monthlyPayDay).val("") if visible("monthlyPayDayWrap")

    if not (val(o.howOftenFrequency) is o.defaultValue or val(o.howOftenFrequency) is "")

      textToUse = switch(val o.howOftenFrequency)
        when "Weekly" then o.usuallyPaidSameAmountWeeklyText
        when "Fortnightly" then o.usuallyPaidSameAmountFortnightlyText
        when "Four-Weekly" then o.usuallyPaidSameAmountFourWeeklyText
        when "Monthly" then o.usuallyPaidSameAmountMonthlyText
        else o.usuallyPaidSameAmountOtherText
      legend(o.usuallyPaidSameAmount).html(textToUse)

      checked o.usuallyPaidSameAmountY,  false
      checked o.usuallyPaidSameAmountN,  false
      S("usuallyPaidSameAmountWrap").slideDown(0)

    S("monthlyPayDayWrap").slideDown(0) if val(o.howOftenFrequency) is "Monthly"

window.usuallyPaidSameAmount = (o) ->
  if val(o.howOftenFrequency) is ""
    S("monthlyPayDayWrap").slideUp 0
    S("usuallyPaidSameAmountWrap").slideUp 0

  S(o.howOftenFrequency).change onUsuallyPaidSameAmount(o)


window.employerOwesYouMoney = (usuallyPaidSameAmountY, usuallyPaidSameAmountN) ->
  if not checked(usuallyPaidSameAmountY)
    S("employerOwesYouMoneyHelpWrap").slideUp 0

  S(usuallyPaidSameAmountY).on "click", -> S("employerOwesYouMoneyHelpWrap").slideDown 0 if checked usuallyPaidSameAmountY
  S(usuallyPaidSameAmountN).on "click", -> S("employerOwesYouMoneyHelpWrap").slideUp 0 if not checked usuallyPaidSameAmountY


window.whatFor = (payIntoPensionY, payIntoPensionN, whatFor) ->
  if not checked(payIntoPensionY)
    S("whatForWrap").slideUp 0

  S(payIntoPensionY).on "click", -> S("whatForWrap").slideDown 0
  S(payIntoPensionN).on "click", -> S("whatForWrap").slideUp 0, -> S(whatFor).val("")


window.whatCosts = (careCostsForThisWorkY, careCostsForThisWorkN, whatCosts) ->
  if not checked(careCostsForThisWorkY)
    S("whatCostsWrap").slideUp 0

  S(careCostsForThisWorkY).on "click", -> S("whatCostsWrap").slideDown 0
  S(careCostsForThisWorkN).on "click", -> S("whatCostsWrap").slideUp 0, -> S(whatCosts).val("")
