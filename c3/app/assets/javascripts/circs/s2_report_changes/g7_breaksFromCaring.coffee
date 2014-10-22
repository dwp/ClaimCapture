window.initEvents = (breakEndedY, breakEndedN, expectStartCaringY, expectStartCaringN, expectStartCaringDontKnow) ->
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
