window.initEvents = (additionalBreaksY, additionalBreaksN, text) ->
  hideAdditionalBreaksWrap = (text) ->
    $("#additionalBreaksWrap").slideUp 0, ->
      $("#" + text).val("")

  showAdditionalBreaksWrap = () ->
    $("#additionalBreaksWrap").slideDown 0

  if not $("#" + additionalBreaksY).prop('checked')
    hideAdditionalBreaksWrap(text)

  if not $("#" + additionalBreaksN).prop('checked')
    showAdditionalBreaksWrap()

  $("#" + additionalBreaksY).on "click", ->
    showAdditionalBreaksWrap()

  $("#" + additionalBreaksN).on "click", ->
    hideAdditionalBreaksWrap(text)

