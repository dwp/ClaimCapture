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


#
# Fix Error Messages function
#

window.fixErrorMessages = (beenPaidYetY, beenPaidYetN,
                           howMuchPaid, howMuchPaidYText, howMuchPaidNText,
                           whatDatePaid, whatDatePaidYText, whatDatePaidNText,
                           howOften, howOftenFrequency, howOftenText, howOftenYText, howOftenNText,
                           usuallyPaidSameAmount, usuallyPaidSameAmountText,
                           usuallyPaidSameAmountYWeekText, usuallyPaidSameAmountNWeekText,
                           usuallyPaidSameAmountYFortnightText, usuallyPaidSameAmountNFortnightText,
                           usuallyPaidSameAmountYFourWeekText, usuallyPaidSameAmountNFourWeekText,
                           usuallyPaidSameAmountYMonthText, usuallyPaidSameAmountNMonthText,
                           usuallyPaidSameAmountYOtherText, usuallyPaidSameAmountNOtherText) ->

  currentText = vssText(howOften)
  vssText(howOften, howOftenNText + subString(currentText, howOftenText, currentText))

  if checked beenPaidYetN
    currentText = vssText(howMuchPaid)
    vssText(howMuchPaid,howMuchPaidNText + subString(currentText, howMuchPaidYText, currentText))

    currentText = vssText(whatDatePaid)
    vssText(whatDatePaid,whatDatePaidNText + subString(currentText, whatDatePaidYText, currentText))

    currentText = vssText(howOften)
    vssText(howOften,howOftenNText + subString(currentText, howOftenText, currentText))

    currentText = vssText(usuallyPaidSameAmount)

    textToUse = switch (val howOftenFrequency)
      when "weekly"       then usuallyPaidSameAmountNWeekText
      when "fortnightly"  then usuallyPaidSameAmountNFortnightText
      when "fourweekly"   then usuallyPaidSameAmountNFourWeekText
      when "monthly"      then usuallyPaidSameAmountNMonthText
      else usuallyPaidSameAmountNOtherText

    vssText(usuallyPaidSameAmount,textToUse + subString(currentText, usuallyPaidSameAmountText, currentText))

  if checked beenPaidYetY
    currentText = vssText(usuallyPaidSameAmount)
    textToUse = switch (val(howOftenFrequency))
      when "weekly" then usuallyPaidSameAmountNWeekText
      when "fortnightly" then usuallyPaidSameAmountNFortnightText
      when "fourweekly" then usuallyPaidSameAmountNFourWeekText
      when "monthly" then usuallyPaidSameAmountNMonthText
      else usuallyPaidSameAmountNOtherText
    vssText(usuallyPaidSameAmount,textToUse + subString(currentText, usuallyPaidSameAmountText, currentText))

    currentText = vssText(howOften)
    vssText(howOften,howOftenYText + subString(currentText, howOftenText, currentText))



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
  $("#" + o.usuallyPaidSameAmountY).prop('checked', false)
  $("#" + o.usuallyPaidSameAmountN).prop('checked', false)

onBeenPaidYetY = (ctx) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                      #The first function exists so the returned function has the "context" variable in scope
    $("#beenPaidYetWrap").slideUp 0, -> hideBeenPaidYetWrapOnY(ctx)
    $("#usuallyPaidSameAmountWrap").slideUp 0
    $("#monthlyPayDayWrap").slideUp 0 if visible ctx.monthlyPayDayWrap
    $("#beenPaidYetWrap").slideDown 0
    $("#howOften_wrap").slideUp(0, -> val ctx.howOftenFrequencyOther , "") if visible ctx.howOften_wrap

onBeenPaidYetN = (ctx) -> #Double -> because it's a function returning a function, which the jQuqery .on function will receive.
  ->                      #The first function exists so the returned function has the "context" variable in scope
    $("#beenPaidYetWrap").slideUp 0, -> hideBeenPaidYetWrapOnN(ctx)
    $("#usuallyPaidSameAmountWrap").slideUp 0
    $("#monthlyPayDayWrap").slideUp 0 if visible ctx.monthlyPayDayWrap
    $("#beenPaidYetWrap").slideDown 0
    $("#howOften_wrap").slideUp(0, -> val ctx.howOftenFrequencyOther, "") if visible ctx.howOften_wrap


#
#Been Paid Yet function
#

window.beenPaidYet = (ctx) ->
  $("#" + ctx.beenPaidYetY).on "click", onBeenPaidYetY(ctx)
  $("#" + ctx.beenPaidYetN).on "click", onBeenPaidYetN(ctx)



#
#Pay Day function
#

window.payDay = (o) ->
  $("#" + o.howOftenFrequency).change ->
    if visible("#usuallyPaidSameAmountWrap")
      $("#usuallyPaidSameAmountWrap").slideUp 0, ->
      checked(o.usuallyPaidSameAmountY,false)
      checked(o.usuallyPaidSameAmountN,false)

    $("#monthlyPayDayWrap").slideUp 0, -> val(o.monthlyPayDay,"") if visible("#monthlyPayDayWrap")

    if val(o.howOftenFrequency) is o.defaultValue or val(o.howOftenFrequency) is ""
    else
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
      $("#usuallyPaidSameAmountWrap").slideDown(0)


    $("#monthlyPayDayWrap").slideDown(0) if val(o.howOftenFrequency) is "monthly"

#
# What For function
#

window.whatFor = (payIntoPensionY, payIntoPensionN, whatFor) ->
  $("#" + payIntoPensionY).on "click", ->
    $("#whatForWrap").slideDown 0
    $("#whatForWrap").css('display', "block")

  $("#" + payIntoPensionN).on "click", ->
    $("#whatForWrap").slideUp 0, ->
      val(whatFor,"")

#
# What Costs function
#
window.whatCosts = (careCostsForThisWorkY, careCostsForThisWorkN, whatCosts) ->
  $("#" + careCostsForThisWorkY).on "click", ->
    $("#whatCostsWrap").slideDown 0
    $("#whatCostsWrap").css('display', "block")

  $("#" + careCostsForThisWorkN).on "click", ->
    $("#whatCostsWrap").slideUp 0, ->
      val(whatCosts,"")
