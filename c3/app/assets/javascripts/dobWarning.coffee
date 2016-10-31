
window.dobWarning = (day, month, year, dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore, testOff) ->
  checkDateOfBirth(getDay(day), getMonth(month), $("#" + year).val() , dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore)

  $("#" + day).on "keyup keypress blur change", ->
    checkDateOfBirth(getDay(day), getMonth(month), $("#" + year).val(), dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore)

  $("#" + month).on "keyup keypress blur change", ->
    checkDateOfBirth(getDay(day), getMonth(month), $("#" + year).val(), dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore)

  $("#" + year).on "keyup keypress blur change", ->
    checkDateOfBirth(getDay(day), getMonth(month), $("#" + year).val(), dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore)

  $("#" + testOn).on "change", ->
    checkDateOfBirth(getDay(day), getMonth(month), $("#" + year).val(), dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore)

  $("#" + testOff).on "change", ->
    checkDateOfBirth(getDay(day), getMonth(month), $("#" + year).val(), dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore)

getDay = (day) ->
  day = $("#" + day).val()
  if (day < 10) then day = "0" + day
  else day

getMonth = (month) ->
  month = $("#" + month).val()
  if (month < 10) then month = "0" + month
  else month

isLegalDate=(day,month,year)->
  if month>12 then false
  else if day>daysInMonth(year,month) then false
  else true

isLeapYear = (year) ->
  (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))

daysInMonth = (year, month) ->
  if(isLeapYear(year)) then  [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month-1]
  else [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month-1]

checkDateOfBirth = (day, month, year, dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore) ->
  if (isLegalDate(day, month, year)) then checkAllDOB(day, month, year, dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore);

checkAllDOB = (day, month, year, dob1, dob2, name1, name2, first, last, warning, pageTag, firstTag, secondTag, testOn, testOnBefore) ->
  if ((day + "-" + month + "-" + year) == dob1)
    txt = $("#" + warning + "-" + firstTag + "-and-" + pageTag + "-message").html().replace("@pagename", $("#" + first).val() + " " + $("#" + last).val()).replace("@name", name1)
    $("#" + warning + "-message").html(txt)
    $("#" + warning).show()
  else if ((day + "-" + month + "-" + year) == dob2 && not $("#" + testOn).prop('checked') && testOnBefore != "yes")
    txt = $("#" + warning + "-" + secondTag + "-and-" + pageTag + "-message").html().replace("@pagename", $("#" + first).val() + " " + $("#" + last).val()).replace("@name", name2)
    $("#" + warning + "-message").html(txt)
    $("#" + warning).show()
  else $("#" + warning).hide()