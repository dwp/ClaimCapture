window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->

  if not $("#" + anythingElseY).prop 'checked'
    hideAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseY).on "click", ->
    showAnythingElseWrapper()

  $("#" + anythingElseN).on "click", ->
    hideAnythingElseWrapper(anythingElseText)

showAnythingElseWrapper = ->
  $("#additionalInfoWrap").slideDown(0).attr 'aria-hidden', 'false'

hideAnythingElseWrapper = (anythingElseText) ->
  $("#additionalInfoWrap").slideUp(0).attr 'aria-hidden', 'true', -> $("#"+anythingElseText).val("")