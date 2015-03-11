window.initEvents = (breakEndedY, breakEndedN, expectStartCaringY, expectStartCaringN, expectStartCaringDontKnow, breakStartDate, breakStartTime, breakEndedDate,breakEndedTime) ->
  if not $("#" + breakEndedY).prop("checked")
    hideBreakEndedDate()

  if not $("#" + breakEndedN).prop("checked")
    hideExpectStartCaring(expectStartCaringY, expectStartCaringN)
    hideExpectStartCaringDate()
    hidePermanentBreakDate()

  if not $("#" + expectStartCaringY).prop("checked")
#    hideExpectStartCaring()
    hideExpectStartCaringDate()
    hidePermanentBreakDate()

  if not $("#" + expectStartCaringN).prop("checked")
    hidePermanentBreakDate()

  if isMondayOrFriday(breakStartDate)
    $("#" + breakStartTime).slideUp 0

  if isMondayOrFriday(breakEndedDate)
    $("#" + breakEndedTime).slideUp 0

  $("#" + breakEndedY).on "click", ->
    showBreakEndedDate()
    hideExpectStartCaring(expectStartCaringY, expectStartCaringN)
    hideExpectStartCaringDate()
    hidePermanentBreakDate()

  $("#" + breakEndedN).on "click", ->
    showExpectStartCaring()
    hideBreakEndedDate()

  $("#" + expectStartCaringY).on "click", ->
    showExpectStartCaringDate()
    hidePermanentBreakDate()

  $("#" + expectStartCaringN).on "click", ->
    showPermanentBreakDate()
    hideExpectStartCaringDate()

  $("#" + expectStartCaringDontKnow).on "click", ->
    hideExpectStartCaringDate()
    hidePermanentBreakDate()

  dateOnClick(breakStartDate,(id)->
    isIt = isMondayOrFriday(id)
    $("#" + breakStartTime).slideUp 0 if isIt
    $("#" + breakStartTime).slideDown 0 if !isIt
  )

  dateOnClick(breakEndedDate,(id)->
    isIt = isMondayOrFriday(id)
    $("#" + breakEndedTime).slideUp 0 if isIt
    $("#" + breakEndedTime).slideDown 0 if !isIt
  )

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

isMondayOrFriday = (id) ->
  date = getDate(id)
  date != undefined and (date.getDay() == 1 or date.getDay() == 5)

dateOnClick = (id,f) ->
  $("#"+id+" li input").on "click", ->
    f(id)


hideBreakEndedDate = () ->
  $("#breakEndedDateTime").slideUp 0

showBreakEndedDate = () ->
  $("#breakEndedDateTime").slideDown 0

hideExpectStartCaring = (expectStartCaringY, expectStartCaringN) ->
  $("#expectStartCaring").slideUp 0
  $("#" + expectStartCaringY).attr("checked", false)
  $("#" + expectStartCaringN).attr("checked", false)

showExpectStartCaring = () ->
  $("#expectStartCaring").slideDown 0

hidePermanentBreakDate = () ->
  $("#permanentBreakDate").slideUp 0

showPermanentBreakDate = () ->
  $("#permanentBreakDate").slideDown 0

hideExpectStartCaringDate = () ->
  $("#expectStartCaringDate").slideUp 0

showExpectStartCaringDate = () ->
  $("#expectStartCaringDate").slideDown 0
