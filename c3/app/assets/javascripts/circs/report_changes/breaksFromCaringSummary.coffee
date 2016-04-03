window.initEvents = (additionalBreaksY, additionalBreaksN, text) ->
  $("#"+text).trigger("blur")

  if $("#" + additionalBreaksY).prop('checked')
    showAdditionalBreaksWrap(text)

  if not $("#" + additionalBreaksY).prop('checked')
    hideAdditionalBreaksWrap(text)

  $("#" + additionalBreaksY).on "click", ->
    showAdditionalBreaksWrap(text)

  $("#" + additionalBreaksN).on "click", ->
    hideAdditionalBreaksWrap(text)

hideAdditionalBreaksWrap = (text) ->
    $("#additionalBreaksWrap").slideUp(0).attr 'aria-hidden', 'true', ->
    $("#additionalBreaksWrap textarea").val("")

showAdditionalBreaksWrap = (text) ->
    $("#"+text).trigger("blur")
    $("#additionalBreaksWrap").slideDown(0).attr 'aria-hidden', 'false'

