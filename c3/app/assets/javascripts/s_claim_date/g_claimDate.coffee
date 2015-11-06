window.initEvents = (spent35HoursCaringBeforeClaimY, spent35HoursCaringBeforeClaimN, day, month, year) ->

  if not $("#" + spent35HoursCaringBeforeClaimY).prop('checked')
    hideCareStartDateWrap(day, month, year)

  $("#" + spent35HoursCaringBeforeClaimY).on "click", ->
    showCareStartDateWrap()

  $("#" + spent35HoursCaringBeforeClaimN).on "click", ->
    hideCareStartDateWrap(day, month, year)


showCareStartDateWrap = ->
  $("#careStartDateWrap").slideDown(0).attr 'aria-hidden', 'false'

hideCareStartDateWrap = (day, month, year) ->
    $("#careStartDateWrap").slideUp(0).attr 'aria-hidden', 'true'
    $("#" + day).val("")
    $("#" + month).val("")
    $("#" + year).val("")


window.initDateWarning = (warningId,day, month, year,text) ->
  hideWarning(warningId)

  $("#"+day).change(initDateWarningOnChange(warningId,day, month, year,text))
  $("#"+month).change(initDateWarningOnChange(warningId,day, month, year,text))
  $("#"+year).change(initDateWarningOnChange(warningId,day, month, year,text))

initDateWarningOnChange = (warningId,day,month,year,text) -> ->
  dayV = $("#"+day).val()
  monthV = $("#"+month).val()
  yearV = $("#"+year).val()
  if (dayV.trim().length> 0 and monthV.trim().length > 0 and yearV.trim().length > 0)
    futureDate = new Date(yearV,monthV,dayV)
    if(new Date().addMonths(3).getTime() <= futureDate.getTime())
      showWarning(warningId)
      trackEvent('/your-claim-date/claim-date', 'Error',text);
    else
      hideWarning(warningId)


showWarning = (warningId) ->
  $("#"+warningId).slideDown(0)

hideWarning = (warningId) ->
  $("#"+warningId).slideUp(0)


#Stripped code from datejs needed to operate with months considering shorter months, leap years... etc
Date.isLeapYear = (year) ->
  (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))


Date.getDaysInMonth = (year, month) ->
  [31, (Date.isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];


Date.prototype.isLeapYear = ->
  Date.isLeapYear(this.getFullYear())


Date.prototype.getDaysInMonth = ->
  Date.getDaysInMonth(this.getFullYear(), this.getMonth())


Date.prototype.addMonths = (value) ->
  n = this.getDate()
  this.setDate(1)
  this.setMonth(this.getMonth() + value)
  this.setDate(Math.min(n, this.getDaysInMonth()))
  this
