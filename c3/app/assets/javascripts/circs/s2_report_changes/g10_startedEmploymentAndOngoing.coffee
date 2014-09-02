#Util functions so the code looks cleaner

#vss stands for validation-summary selector
vss = (href) -> $("div[class='validation-summary'] a[href='##{href}']")
vssText = (href,text)     -> if text? then vss(href).text(text) else vss(href).text().trim()
checked = (selector,val)  -> if val?  then $("##{selector}").prop('checked',val) else $("##{selector}").prop('checked')
val = (selector,text)     -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
subString = (str,start,end) -> str.substring(start.length,end.length)
visible = (selector) -> $("##{selector}").is(":visible")
legend = (selector)-> $("##{selector}").parent().find("legend")
label = (selector) -> $("label[for='#{selector}']")
S = (selector) -> $("##{selector}")


#
# Fix Error Messages function
#

window.fixErrorMessages = (o) ->

  currentText = vssText(o.howOften)
  vssText(o.howOften, o.howOftenNText + subString(currentText, o.howOftenText, currentText))

  if checked o.beenPaidYetN
    currentText = vssText(o.howMuchPaid)
    vssText(o.howMuchPaid,o.howMuchPaidNText + subString(currentText, o.howMuchPaidYText, currentText))

    currentText = vssText(o.whatDatePaid)
    vssText(o.whatDatePaid,o.whatDatePaidNText + subString(currentText, o.whatDatePaidYText, currentText))

    currentText = vssText(o.howOften)
    vssText(o.howOften,o.howOftenNText + subString(currentText, o.howOftenText, currentText))

    currentText = vssText(o.usuallyPaidSameAmount)

    textToUse = switch (val o.howOftenFrequency)
      when "weekly"       then o.usuallyPaidSameAmountNWeekText
      when "fortnightly"  then o.usuallyPaidSameAmountNFortnightText
      when "fourweekly"   then o.usuallyPaidSameAmountNFourWeekText
      when "monthly"      then o.usuallyPaidSameAmountNMonthText
      else o.usuallyPaidSameAmountNOtherText

    vssText(o.usuallyPaidSameAmount,textToUse + subString(currentText, o.usuallyPaidSameAmountText, currentText))

  if checked o.beenPaidYetY
    currentText = vssText(o.usuallyPaidSameAmount)
    textToUse = switch (val(o.howOftenFrequency))
      when "weekly" then o.usuallyPaidSameAmountNWeekText
      when "fortnightly" then o.usuallyPaidSameAmountNFortnightText
      when "fourweekly" then o.usuallyPaidSameAmountNFourWeekText
      when "monthly" then o.usuallyPaidSameAmountNMonthText
      else o.usuallyPaidSameAmountNOtherText
    vssText(o.usuallyPaidSameAmount,textToUse + subString(currentText, o.usuallyPaidSameAmountText, currentText))

    currentText = vssText(o.howOften)
    vssText(o.howOften,o.howOftenYText + subString(currentText, o.howOftenText, currentText))



hideBeenPaidYetWrapOnY = (o) ->
  label(o.howMuchPaid).text(o.howMuchPaidYText)
  val(o.howMuchPaid,"")
  legend(o.whatDatePaid).html(o.whatDatePaidYText)
  val(o.whatDatePaidDay,"")
  val(o.whatDatePaidMonth,"")
  val(o.whatDatePaidYear,"")
  label(o.howOften).text(o.howOftenYText)
  val(o.howOften,"")
  label(o.monthlyPayDay).text(o.monthlyPayDayYText)
  val(o.monthlyPayDay,"")
  textToUse = switch (val(o.howOften))
    when "weekly" then o.usuallyPaidSameAmountWeeklyText
    when "fortnightly" then o.usuallyPaidSameAmountFortnightlyText
    when "fourweekly" then o.usuallyPaidSameAmountFourWeeklyText
    when "monthly" then o.usuallyPaidSameAmountMonthlyText
    else o.usuallyPaidSameAmountOtherText

  legend(usuallyPaidSameAmount).html(textToUse)
  checked(o.usuallyPaidSameAmountY, false)
  checked(o.usuallyPaidSameAmountN, false)

hideBeenPaidYetWrapOnN = (o) ->
  label(o.howMuchPaid).text(o.howMuchPaidNText)
  val(o.howMuchPaid,"")
  legend(o.whatDatePaid).html(o.whatDatePaidNText)
  val(o.whatDatePaidDay,"")
  val(o.whatDatePaidMonth,"")
  val(o.whatDatePaidYear,"")
  label(o.howOften).text(o.howOftenNText)
  val(o.howOften,"")
  label(o.monthlyPayDay).text(o.monthlyPayDayNText)
  val(o.monthlyPayDay,"")
  textToUse = switch (val(o.howOften))
    when "weekly" then o.usuallyPaidSameAmountWeeklyTextExpected
    when "fortnightly" then o.usuallyPaidSameAmountFortnightlyTextExpected
    when "fourweekly" then o.usuallyPaidSameAmountFourWeeklyTextExpected
    when "monthly" then o.usuallyPaidSameAmountMonthlyTextExpected
    else o.usuallyPaidSameAmountOtherTextExpected

  legend(usuallyPaidSameAmount).html(textToUse)
  checked(o.usuallyPaidSameAmountY, false)
  checked(o.usuallyPaidSameAmountN, false)

onBeenPaidYetY = (ctx) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                      #The first function exists so the returned function has the "context" variable in scope
    S("beenPaidYetWrap").slideUp 0, -> hideBeenPaidYetWrapOnY(ctx)
    S("usuallyPaidSameAmountWrap").slideUp 0
    S("monthlyPayDayWrap").slideUp 0 if visible ctx.monthlyPayDayWrap
    S("beenPaidYetWrap").slideDown 0
    S("howOften_wrap").slideUp(0, -> val ctx.howOftenFrequencyOther , "") if visible ctx.howOften_wrap

onBeenPaidYetN = (ctx) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                      #The first function exists so the returned function has the "context" variable in scope
    S("beenPaidYetWrap").slideUp 0, -> hideBeenPaidYetWrapOnN(ctx)
    S("usuallyPaidSameAmountWrap").slideUp 0
    S("monthlyPayDayWrap").slideUp 0 if visible ctx.monthlyPayDayWrap
    S("beenPaidYetWrap").slideDown 0
    S("howOften_wrap").slideUp(0, -> val ctx.howOftenFrequencyOther, "") if visible ctx.howOften_wrap


#
#Been Paid Yet function
#

window.beenPaidYet = (ctx) ->
  S(ctx.beenPaidYetY).on "click", onBeenPaidYetY(ctx)
  S(ctx.beenPaidYetN).on "click", onBeenPaidYetN(ctx)



#
#Pay Day function
#

window.payDay = (o) ->
  S(o.howOftenFrequency).change ->
    if visible("usuallyPaidSameAmountWrap")
      S("usuallyPaidSameAmountWrap").slideUp 0, ->
        checked(o.usuallyPaidSameAmountY,false)
        checked(o.usuallyPaidSameAmountN,false)

    S("monthlyPayDayWrap").slideUp 0, -> val(o.monthlyPayDay,"") if visible("monthlyPayDayWrap")

    if not (val(o.howOftenFrequency) is o.defaultValue or val(o.howOftenFrequency) is "")
      if checked(o.beenPaidYetN)
        textToUse = switch (val(o.howOftenFrequency))
          when "weekly" then o.usuallyPaidSameAmountWeeklyTextExpected
          when "fortnightly" then o.usuallyPaidSameAmountFortnightlyTextExpected
          when "fourweekly" then o.usuallyPaidSameAmountFourWeeklyTextExpected
          when "monthly" then o.usuallyPaidSameAmountMonthlyTextExpected
          else o.usuallyPaidSameAmountOtherTextExpected
        legend(o.usuallyPaidSameAmount).html(textToUse)
          
      if checked(o.beenPaidYetY)
        textToUse = switch (val(o.howOftenFrequency))
          when "weekly" then o.usuallyPaidSameAmountWeeklyText
          when "fortnightly" then o.usuallyPaidSameAmountFortnightlyText
          when "fourweekly" then o.usuallyPaidSameAmountFourWeeklyText
          when "monthly" then o.usuallyPaidSameAmountMonthlyText
          else o.usuallyPaidSameAmountOtherText
        legend(o.usuallyPaidSameAmount).html(textToUse)

      checked(o.usuallyPaidSameAmountY, false)
      checked(o.usuallyPaidSameAmountN, false)
      S("usuallyPaidSameAmountWrap").slideDown(0)


    S("monthlyPayDayWrap").slideDown(0) if val(o.howOftenFrequency) is "monthly"

#
# What For function
#

window.whatFor = (payIntoPensionY, payIntoPensionN, whatFor) ->
  S(payIntoPensionY).on "click", -> S("whatForWrap").slideDown 0

  S(payIntoPensionN).on "click", -> S("whatForWrap").slideUp 0, -> val(whatFor,"")

#
# What Costs function
#
window.whatCosts = (careCostsForThisWorkY, careCostsForThisWorkN, whatCosts) ->
  S(careCostsForThisWorkY).on "click", -> S("whatCostsWrap").slideDown 0

  S(careCostsForThisWorkN).on "click", -> S("whatCostsWrap").slideUp 0, -> val(whatCosts,"")
