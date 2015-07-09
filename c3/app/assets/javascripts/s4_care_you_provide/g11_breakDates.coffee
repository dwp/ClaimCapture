window.initEvents = (breakStartDate, breakEndedDate, breakStartTime, breakEndedTime, doNotKnowEndDate) ->
  if isNotMondayOrFriday(breakStartDate)
    hideTime(breakStartTime)

  if isNotMondayOrFriday(breakEndedDate)
    hideTime(breakEndedTime)

  dateOnChange(breakStartDate,(id)->
    isIt = isNotMondayOrFriday(id)
    hideTime(breakStartTime) if isIt
    showTime(breakStartTime) if not isIt
  )

  dateOnChange(breakEndedDate,(id)->
    isIt = isNotMondayOrFriday(id)
    hideTime(breakEndedTime) if isIt
    showTime(breakEndedTime) if not isIt
  )

  onCheckDoNotKnowEndDate(doNotKnowEndDate, breakEndedDate,(doNotKnowEndDateId, breakEndedDateId)->
    if document.getElementById(doNotKnowEndDateId).checked
      hideTime(breakEndedTime)
    else
      if isNotMondayOrFriday(breakEndedDateId)
        hideTime(breakEndedTime)
      else
        showTime(breakEndedTime)
  )

isNotMondayOrFriday = (id) ->
  date = getDate(id)
  not (date != undefined and (date.getDay() == 1 or date.getDay() == 5))

getDate = (id) ->
  values = $("#"+id+" li input").map( ->
    $(this).val()
  )
  m = parseInt(values[1],10)
  d = parseInt(values[0],10)
  y = parseInt(values[2],10)
  date = new Date(y,m-1,d)
  if date.getFullYear() == y and date.getMonth() + 1 == m and date.getDate() == d
    date
   else
    undefined

hideTime = (id) ->
  $("#"+id).parent("li").slideUp(0).attr 'aria-hidden', 'true'

showTime = (id) ->
  $("#"+id).parent("li").slideDown(0).attr 'aria-hidden', 'false'

dateOnChange = (id,f) ->
  $("#"+id+" li input").on "change paste keyup", ->
    f(id)

onCheckDoNotKnowEndDate = (doNotKnowEndDate, breakEndedDate, f) ->
  $("#"+doNotKnowEndDate).on "click", ->
    f(doNotKnowEndDate, breakEndedDate)