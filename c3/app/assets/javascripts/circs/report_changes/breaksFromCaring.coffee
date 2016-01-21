window.initEvents = (breakEndedY, breakEndedN, expectStartCaringY, expectStartCaringN, expectStartCaringDontKnow,
                     breakStartDate, breakStartTime, breakEndedDate,breakEndedTime,
                     wherePersonBreaksInCare_text, whereYouBreaksInCare_text, moreAboutChanges) ->
  $("#"+wherePersonBreaksInCare_text).trigger("blur")
  $("#"+whereYouBreaksInCare_text).trigger("blur")
  $("#"+moreAboutChanges).trigger("blur")

  if not $("#" + breakEndedY).prop("checked")
    hideBreakEndedDate()

  if not $("#" + breakEndedN).prop("checked")
    hideExpectStartCaring(expectStartCaringY, expectStartCaringN)
    hideExpectStartCaringDate()
    hidePermanentBreakDate()

  if not $("#" + expectStartCaringY).prop("checked")
    hideExpectStartCaringDate()
    hidePermanentBreakDate()

  if not $("#" + expectStartCaringN).prop("checked")
    hidePermanentBreakDate()

  if isNotMondayOrFriday(breakStartDate)
    hideTime(breakStartTime)

  if isNotMondayOrFriday(breakEndedDate)
    hideTime(breakEndedTime)

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

isNotMondayOrFriday = (id) ->
  date = getDate(id)
  not (date != undefined and (date.getDay() == 1 or date.getDay() == 5))

dateOnChange = (id,f) ->
  $("#"+id+" li input").on "change paste keyup", ->
    f(id)

hideTime = (id) ->
  $("#"+id).parent("li").slideUp(0).attr 'aria-hidden', 'true'

showTime = (id) ->
  $("#"+id).parent("li").slideDown(0).attr 'aria-hidden', 'false'

hideBreakEndedDate = () ->
  $("#breakEndedDateTime").slideUp(0).attr 'aria-hidden', 'true'
  $("#breakEndedDateTime input").val("")

showBreakEndedDate = () ->
  $("#breakEndedDateTime").slideDown(0).attr 'aria-hidden', 'false'

hideExpectStartCaring = (expectStartCaringY, expectStartCaringN) ->
  $("#expectStartCaring").slideUp(0).attr 'aria-hidden', 'true'
  $("#" + expectStartCaringY).attr("checked", false)
  $("#" + expectStartCaringN).attr("checked", false)

showExpectStartCaring = () ->
  $("#expectStartCaring").slideDown(0).attr 'aria-hidden', 'false'

hidePermanentBreakDate = () ->
  $("#permanentBreakDate").slideUp(0).attr 'aria-hidden', 'true'
  $("#permanentBreakDate input").val("")

showPermanentBreakDate = () ->
  $("#permanentBreakDate").slideDown(0).attr 'aria-hidden', 'false'

hideExpectStartCaringDate = () ->
  $("#expectStartCaringDate").slideUp(0).attr 'aria-hidden', 'true'
  $("#expectStartCaringDate input").val("")

showExpectStartCaringDate = () ->
  $("#expectStartCaringDate").slideDown(0).attr 'aria-hidden', 'false'
