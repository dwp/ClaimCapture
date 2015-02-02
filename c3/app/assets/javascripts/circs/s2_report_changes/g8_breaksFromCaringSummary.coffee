window.initEvents = (additionalBreaksY, additionalBreaksN, text) ->
  hideAdditionalBreaksWrap = (text) ->
    $("#additionalBreaksWrap").slideUp 0, ->
      $("#" + text).val("")

  showAdditionalBreaksWrap = () ->
    $("#additionalBreaksWrap").slideDown 0

  if $("#" + additionalBreaksY).prop('checked')
    showAdditionalBreaksWrap()

  if not $("#" + additionalBreaksY).prop('checked')
    hideAdditionalBreaksWrap(text)

  $("#" + additionalBreaksY).on "click", ->
    showAdditionalBreaksWrap()

  $("#" + additionalBreaksN).on "click", ->
    hideAdditionalBreaksWrap(text)

