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
labelText = (selector,text) ->
  labelChildren = label(selector).children()
  label(selector).text(text).append(labelChildren)
S = (selector) -> $("##{selector}")


#
# Fix Error Messages function
#

window.fixErrorMessages = (o) ->

  if checked o.beenPaidYetN
    currentText = vssText(o.howMuchPaid)
    vssText(o.howMuchPaid,o.howMuchPaidNText + subString(currentText, o.howMuchPaidYText, currentText))

    currentText = vssText(o.whatDatePaid)
    vssText(o.whatDatePaid,o.whatDatePaidNText + subString(currentText, o.whatDatePaidYText, currentText))

    currentText = vssText(o.howOften)
    vssText(o.howOften,o.howOftenNText + subString(currentText, o.howOftenText, currentText))

    currentText = vssText(o.usuallyPaidSameAmount)

    textToUse = switch (val o.howOftenFrequency)
      when "Weekly"       then o.usuallyPaidSameAmountNWeekText
      when "Fortnightly"  then o.usuallyPaidSameAmountNFortnightText
      when "Four-Weekly"   then o.usuallyPaidSameAmountNFourWeekText
      when "Monthly"      then o.usuallyPaidSameAmountNMonthText
      else o.usuallyPaidSameAmountNOtherText

    vssText(o.usuallyPaidSameAmount,textToUse + subString(currentText, o.usuallyPaidSameAmountText, currentText))

  if checked o.beenPaidYetY
    currentText = vssText(o.usuallyPaidSameAmount)
    textToUse = switch (val(o.howOftenFrequency))
      when "Weekly" then o.usuallyPaidSameAmountNWeekText
      when "Fortnightly" then o.usuallyPaidSameAmountNFortnightText
      when "Four-Weekly" then o.usuallyPaidSameAmountNFourWeekText
      when "Monthly" then o.usuallyPaidSameAmountNMonthText
      else o.usuallyPaidSameAmountNOtherText
    vssText(o.usuallyPaidSameAmount,textToUse + subString(currentText, o.usuallyPaidSameAmountText, currentText))

    currentText = vssText(o.howOften)
    vssText(o.howOften,o.howOftenYText + subString(currentText, o.howOftenText, currentText))



hideBeenPaidYetWrapOnY = (o,reset) ->
  labelText(o.howMuchPaid,o.howMuchPaidYText)
  val(o.howMuchPaid,"") if reset
  legend(o.whatDatePaid).html(o.whatDatePaidYText)
  val(o.whatDatePaidDay,"") if reset
  val(o.whatDatePaidMonth,"") if reset
  val(o.whatDatePaidYear,"") if reset
  labelText(o.howOften,o.howOftenYText)
  val(o.howOften,"") if reset
  labelText(o.monthlyPayDay,o.monthlyPayDayYText)
  val(o.monthlyPayDay,"") if reset
  textToUse = switch (val(o.howOften))
    when "Weekly" then o.usuallyPaidSameAmountWeeklyText
    when "Fortnightly" then o.usuallyPaidSameAmountFortnightlyText
    when "Four-Weekly" then o.usuallyPaidSameAmountFourWeeklyText
    when "Monthly" then o.usuallyPaidSameAmountMonthlyText
    else o.usuallyPaidSameAmountOtherText

  legend(o.usuallyPaidSameAmount).html(textToUse)
  checked(o.usuallyPaidSameAmountY, false) if reset
  checked(o.usuallyPaidSameAmountN, false) if reset
  datePaidContextualHelp(o,true)

hideBeenPaidYetWrapOnN = (o,reset) ->
  labelText(o.howMuchPaid,o.howMuchPaidNText)
  val(o.howMuchPaid,"") if reset
  legend(o.whatDatePaid).html(o.whatDatePaidNText)
  val(o.whatDatePaidDay,"") if reset
  val(o.whatDatePaidMonth,"") if reset
  val(o.whatDatePaidYear,"") if reset
  labelText(o.howOften,o.howOftenNText)
  val(o.howOften,"") if reset
  labelText(o.monthlyPayDay,o.monthlyPayDayNText)
  val(o.monthlyPayDay,"") if reset
  textToUse = switch (val(o.howOften))
    when "Weekly" then o.usuallyPaidSameAmountWeeklyTextExpected
    when "Fortnightly" then o.usuallyPaidSameAmountFortnightlyTextExpected
    when "Four-Weekly" then o.usuallyPaidSameAmountFourWeeklyTextExpected
    when "Monthly" then o.usuallyPaidSameAmountMonthlyTextExpected
    else o.usuallyPaidSameAmountOtherTextExpected

  legend(o.usuallyPaidSameAmount).html(textToUse)
  checked(o.usuallyPaidSameAmountY, false) if reset
  checked(o.usuallyPaidSameAmountN, false) if reset
  datePaidContextualHelp(o,false)

datePaidContextualHelp = (o,beenPaidYet) ->
  if (beenPaidYet)
      S(o.whatDatePaid + "_defaultDateContextualHelp").css('display', "block")
      S(o.whatDatePaid + "_alternativeDateContextualHelp").css('display', "none")
  else
    S(o.whatDatePaid + "_defaultDateContextualHelp").css('display', "none")
    S(o.whatDatePaid + "_alternativeDateContextualHelp").css('display', "block")

hideBeenPaid = (ctx,reset,hideFunction) ->
  S("beenPaidYetWrap").slideUp 0, -> hideFunction(ctx,reset)
  S("usuallyPaidSameAmountWrap").slideUp 0
  S("monthlyPayDayWrap").slideUp 0
  S("beenPaidYetWrap").slideDown 0
  if not (val(ctx.howOften) is "Other")
    S("howOften_wrap").slideUp(0, -> (val ctx.howOftenFrequencyOther, "") if reset)

onBeenPaidYetY = (ctx,reset) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                      #The first function exists so the returned function has the "context" variable in scope
    hideBeenPaid(ctx,reset,hideBeenPaidYetWrapOnY)

onBeenPaidYetN = (ctx,reset) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                      #The first function exists so the returned function has the "context" variable in scope
    hideBeenPaid(ctx,reset,hideBeenPaidYetWrapOnN)


#
#Been Paid Yet function
#

window.beenPaidYet = (ctx) ->
  if checked(ctx.beenPaidYetY)
    onBeenPaidYetY(ctx,false)()
  else if checked(ctx.beenPaidYetN)
    onBeenPaidYetN(ctx,false)()
  else
    S("beenPaidYetWrap").slideUp 0, -> hideBeenPaidYetWrapOnN(ctx,false)
    S("usuallyPaidSameAmountWrap").slideUp 0
    S("monthlyPayDayWrap").slideUp 0

  S(ctx.beenPaidYetY).on "click", onBeenPaidYetY(ctx,true)
  S(ctx.beenPaidYetN).on "click", onBeenPaidYetN(ctx,true)


  
  
onHowOftenFrequency = (ctx,reset) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                           #The first function exists so the returned function has the "context" variable in scope
    if visible("usuallyPaidSameAmountWrap")
      S("usuallyPaidSameAmountWrap").slideUp 0, ->
        checked(ctx.usuallyPaidSameAmountY,false) if reset
        checked(ctx.usuallyPaidSameAmountN,false) if reset

    S("monthlyPayDayWrap").slideUp 0, -> val(ctx.monthlyPayDay,"") if visible("monthlyPayDayWrap") and reset

    if not (val(ctx.howOftenFrequency) is ctx.defaultValue or val(ctx.howOftenFrequency) is "")
      if checked(ctx.beenPaidYetN)
        textToUse = switch (val(ctx.howOftenFrequency))
          when "Weekly" then ctx.usuallyPaidSameAmountWeeklyTextExpected
          when "Fortnightly" then ctx.usuallyPaidSameAmountFortnightlyTextExpected
          when "Four-Weekly" then ctx.usuallyPaidSameAmountFourWeeklyTextExpected
          when "Monthly" then ctx.usuallyPaidSameAmountMonthlyTextExpected
          else ctx.usuallyPaidSameAmountOtherTextExpected
        legend(ctx.usuallyPaidSameAmount).html(textToUse)

      if checked(ctx.beenPaidYetY)
        textToUse = switch (val(ctx.howOftenFrequency))
          when "Weekly" then ctx.usuallyPaidSameAmountWeeklyText
          when "Fortnightly" then ctx.usuallyPaidSameAmountFortnightlyText
          when "Four-Weekly" then ctx.usuallyPaidSameAmountFourWeeklyText
          when "Monthly" then ctx.usuallyPaidSameAmountMonthlyText
          else ctx.usuallyPaidSameAmountOtherText
        legend(ctx.usuallyPaidSameAmount).html(textToUse)

      checked(ctx.usuallyPaidSameAmountY, false) if reset
      checked(ctx.usuallyPaidSameAmountN, false) if reset
      S("usuallyPaidSameAmountWrap").slideDown(0)


    S("monthlyPayDayWrap").slideDown(0) if val(ctx.howOftenFrequency) is "Monthly"

#
#Pay Day function
#

window.payDay = (o) ->
  onHowOftenFrequency(o,false)()
  S(o.howOftenFrequency).on "change", onHowOftenFrequency(o,true)

#
# What For function
#

window.whatFor = (payIntoPensionY, payIntoPensionN, whatFor) ->
  if not checked(payIntoPensionY)
    S("whatForWrap").slideUp 0, -> val(whatFor,"")
  S(payIntoPensionY).on "click", -> S("whatForWrap").slideDown 0

  S(payIntoPensionN).on "click", -> S("whatForWrap").slideUp 0, -> val(whatFor,"")


#
# Pay For things
#

window.whatThings = (payForThingsY, payForThingsN, whatThings) ->
  if not checked(payForThingsY)
    S("whatThingsWrap").slideUp 0, -> val(whatThings,"")
  S(payForThingsY).on "click", -> S("whatThingsWrap").slideDown 0

  S(payForThingsN).on "click", -> S("whatThingsWrap").slideUp 0, -> val(whatThings,"")


#
# What Costs function
#
window.whatCosts = (careCostsForThisWorkY, careCostsForThisWorkN, whatCosts) ->
  if not checked(careCostsForThisWorkY)
    S("whatCostsWrap").slideUp 0, -> val(whatCosts,"")

  S(careCostsForThisWorkY).on "click", -> S("whatCostsWrap").slideDown 0

  S(careCostsForThisWorkN).on "click", -> S("whatCostsWrap").slideUp 0, -> val(whatCosts,"")
