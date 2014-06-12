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
  currentText = $("div[class='validation-summary'] a[href='#" + howOften + "']").text().trim()
  existingError = currentText.substring(howOftenText.length, currentText.length)
  $("div[class='validation-summary'] a[href='#" + howOften + "']").text(howOftenNText + existingError)
  if ($("#" + beenPaidYetN).prop('checked'))
    currentText = $("div[class='validation-summary'] a[href='#" + howMuchPaid + "']").text().trim()
    existingError = currentText.substring(howMuchPaidYText.length, currentText.length)
    $("div[class='validation-summary'] a[href='#" + howMuchPaid + "']").text(howMuchPaidNText + existingError)
    currentText = $("div[class='validation-summary'] a[href='#" + whatDatePaid + "']").text().trim()
    existingError = currentText.substring(whatDatePaidYText.length, currentText.length)
    $("div[class='validation-summary'] a[href='#" + whatDatePaid + "']").text(whatDatePaidNText + existingError)
    currentText = $("div[class='validation-summary'] a[href='#" + howOften + "']").text().trim()
    existingError = currentText.substring(howOftenText.length, currentText.length)
    $("div[class='validation-summary'] a[href='#" + howOften + "']").text(howOftenNText + existingError)
    currentText = $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text().trim()
    existingError = currentText.substring(usuallyPaidSameAmountText.length, currentText.length)
    textToUse = switch ($("#" + howOftenFrequency).val())
      when "weekly" then usuallyPaidSameAmountNWeekText
      when "fortnightly" then usuallyPaidSameAmountNFortnightText
      when "fourweekly" then usuallyPaidSameAmountNFourWeekText
      when "monthly" then usuallyPaidSameAmountNMonthText
      else usuallyPaidSameAmountNOtherText
    $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text(textToUse + existingError)
  if ($("#" + beenPaidYetY).prop('checked'))
    currentText = $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text().trim()
    existingError = currentText.substring(usuallyPaidSameAmountText.length, currentText.length)
    textToUse = switch ($("#" + howOftenFrequency).val())
      when "weekly" then usuallyPaidSameAmountNWeekText
      when "fortnightly" then usuallyPaidSameAmountNFortnightText
      when "fourweekly" then usuallyPaidSameAmountNFourWeekText
      when "monthly" then usuallyPaidSameAmountNMonthText
      else usuallyPaidSameAmountNOtherText
    $("div[class='validation-summary'] a[href='#" + usuallyPaidSameAmount + "']").text(textToUse + existingError)
    currentText = $("div[class='validation-summary'] a[href='#" + howOften + "']").text().trim()
    existingError = currentText.substring(howOftenText.length, currentText.length)
    $("div[class='validation-summary'] a[href='#" + howOften + "']").text(howOftenYText + existingError)

window.beenPaidYet = (beenPaidYetY, beenPaidYetN,
                      howMuchPaid, howMuchPaidYText, howMuchPaidNText,
                      whatDatePaid, whatDatePaidDay, whatDatePaidMonth, whatDatePaidYear, whatDatePaidYText, whatDatePaidNText,
                      howOften, howOftenFrequencyOther, howOftenYText, howOftenNText,
                      monthlyPayDay, monthlyPayDayYText, monthlyPayDayNText,
                      usuallyPaidSameAmount, usuallyPaidSameAmountY, usuallyPaidSameAmountN,
                      usuallyPaidSameAmountWeeklyText, usuallyPaidSameAmountWeeklyTextExpected,
                      usuallyPaidSameAmountFortnightlyText, usuallyPaidSameAmountFortnightlyTextExpected,
                      usuallyPaidSameAmountFourWeeklyText, usuallyPaidSameAmountFourWeeklyTextExpected,
                      usuallyPaidSameAmountMonthlyText, usuallyPaidSameAmountMonthlyTextExpected,
                      usuallyPaidSameAmountOtherText, usuallyPaidSameAmountOtherTextExpected) ->
  $("#" + beenPaidYetY).on "click", ->
    $("#beenPaidYetWrap").slideUp 500, ->
      $("#beenPaidYetWrap").css('display', "none")
      $("label[for='" + howMuchPaid + "']").text(howMuchPaidYText)
      $("#" + howMuchPaid).val("")
      $("#" + whatDatePaid).parent().find("legend").html(whatDatePaidYText)
      $("#" + whatDatePaidDay).val("")
      $("#" + whatDatePaidMonth).val("")
      $("#" + whatDatePaidYear).val("")
      $("label[for='" + howOften + "']").text(howOftenYText)
      $("#" + howOften).val("")
      $("label[for='" + monthlyPayDay + "']").text(monthlyPayDayYText)
      $("#" + monthlyPayDay).val("")
      if ($("#" + howOften).val() == "weekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountWeeklyText)
      if ($("#" + howOften).val() == "fortnightly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFortnightlyText)
      if ($("#" + howOften).val() == "fourWeekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFourWeeklyText)
      if ($("#" + howOften).val() == "monthly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountMonthlyText)
      if ($("#" + howOften).val() == "other")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountOtherText)
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
      $("#" + whatDatePaid).parent().find("legend").html(whatDatePaidNText)
      $("#" + whatDatePaidDay).val("")
      $("#" + whatDatePaidMonth).val("")
      $("#" + whatDatePaidYear).val("")
      $("label[for='" + howOften + "']").text(howOftenNText)
      $("#" + howOften).val("")
      $("label[for='" + monthlyPayDay + "']").text(monthlyPayDayNText)
      $("#" + monthlyPayDay).val("")
      if ($("#" + howOften).val() == "weekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountWeeklyTextExpected)
      if ($("#" + howOften).val() == "fortnightly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFortnightlyTextExpected)
      if ($("#" + howOften).val() == "fourWeekly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFourWeeklyTextExpected)
      if ($("#" + howOften).val() == "monthly")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountMonthlyTextExpected)
      if ($("#" + howOften).val() == "other")
        $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountOtherTextExpected)
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

window.payDay = (beenPaidYetY, beenPaidYetN, howOftenFrequency, monthlyPayDay,
                 usuallyPaidSameAmount, usuallyPaidSameAmountY, usuallyPaidSameAmountN, defaultValue,
                 usuallyPaidSameAmountWeeklyText, usuallyPaidSameAmountWeeklyTextExpected,
                 usuallyPaidSameAmountFortnightlyText, usuallyPaidSameAmountFortnightlyTextExpected,
                 usuallyPaidSameAmountFourWeeklyText, usuallyPaidSameAmountFourWeeklyTextExpected,
                 usuallyPaidSameAmountMonthlyText, usuallyPaidSameAmountMonthlyTextExpected,
                 usuallyPaidSameAmountOtherText, usuallyPaidSameAmountOtherTextExpected) ->
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
      if ($("#" + beenPaidYetN).prop('checked'))
        if ($("#" + howOftenFrequency).val() == "weekly")
          $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountWeeklyTextExpected)
        if ($("#" + howOftenFrequency).val() == "fortnightly")
          $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFortnightlyTextExpected)
        if ($("#" + howOftenFrequency).val() == "fourWeekly")
          $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountFourWeeklyTextExpected)
        if ($("#" + howOftenFrequency).val() == "monthly")
          $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountMonthlyTextExpected)
        if ($("#" + howOftenFrequency).val() == "other")
          $("#" + usuallyPaidSameAmount).parent().find("legend").html(usuallyPaidSameAmountOtherTextExpected)
      if ($("#" + beenPaidYetY).prop('checked'))
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
