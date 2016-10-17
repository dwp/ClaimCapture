window.initEvents = (spent35HoursCaringBeforeClaimY, spent35HoursCaringBeforeClaimN, day, month, year) ->
  if not $("#" + spent35HoursCaringBeforeClaimY).prop('checked')
    hideCareStartDateWrap(day, month, year)
  $("#" + spent35HoursCaringBeforeClaimY).on "click", ->showCareStartDateWrap()
  $("#" + spent35HoursCaringBeforeClaimN).on "click", ->hideCareStartDateWrap(day, month, year)

showCareStartDateWrap = ->
  $("#careStartDateWrap").slideDown(0).attr 'aria-hidden', 'false'

hideCareStartDateWrap = (day, month, year) ->
    $("#careStartDateWrap").slideUp(0).attr 'aria-hidden', 'true'
    $("#" + day).val("")
    $("#" + month).val("")
    $("#" + year).val("")

window.initDateWarning = (warningId,day, month, year,text,testMode) ->
  $("#"+day).on "change keyup",->initDateWarningOnChange(warningId,day, month, year,text,testMode)
  $("#"+month).on "change keyup",->initDateWarningOnChange(warningId,day, month, year,text,testMode)
  $("#"+year).on "change keyup",->initDateWarningOnChange(warningId,day, month, year,text,testMode)
  initDateWarningOnChange(warningId,day, month, year,text,testMode)

initDateWarningOnChange = (warningId,day,month,year,text,testMode) ->
  dayV = $("#"+day).val()
  monthV = $("#"+month).val()
  yearV = $("#"+year).val()
  showWarningMsg=false
  if (dayV.length> 0 and monthV.length > 0 and yearV.length > 0)
    futureDate = new Date(yearV,monthV-1,dayV)
    if(isLegalDate(dayV,monthV,yearV) && Date.today().add(3).months().getTime() < futureDate.getTime())
      showWarningMsg=true

  if(showWarningMsg)
    $("#"+warningId).slideDown(0)
    if not testMode then trackEvent('/your-claim-date/claim-date', 'Error',text);
  else
    $("#"+warningId).slideUp(0)

isLegalDate=(day,month,year)->
  if month>12 then false
  else if day>daysInMonth(year,month) then false
  else true

isLeapYear = (year) ->
  (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))

daysInMonth = (year, month) ->
  if(isLeapYear(year)) then  [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month-1]
  else [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month-1]



