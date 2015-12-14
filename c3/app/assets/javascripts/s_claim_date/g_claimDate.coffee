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


window.initDateWarning = (warningId,day, month, year,text,testMode) ->
  hideWarning(warningId)
  f = initDateWarningOnChange(warningId,day, month, year,text,testMode)

  $("#"+day).on("change",f)
  $("#"+day).on("keyup",f)
  $("#"+month).on("change",f)
  $("#"+month).on("keyup",f)
  $("#"+year).on("change",f)
  $("#"+year).on("keyup",f)

initDateWarningOnChange = (warningId,day,month,year,text,testMode) -> ->
  dayV = $("#"+day).val()
  monthV = $("#"+month).val()
  yearV = $("#"+year).val()
  if (dayV.length> 0 and monthV.length > 0 and yearV.length > 0)
    futureDate = new Date(yearV,monthV-1,dayV)
    if(Date.today().add(3).months().getTime() <= futureDate.getTime())
      showWarning(warningId)
      if not testMode then trackEvent('/your-claim-date/claim-date', 'Error',text);
    else
      hideWarning(warningId)


showWarning = (warningId) ->
  $("#"+warningId).slideDown(0)

hideWarning = (warningId) ->
  $("#"+warningId).slideUp(0)



