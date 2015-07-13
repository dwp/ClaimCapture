window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->

  if not $("#" + anythingElseY).prop 'checked'
    hideAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseY).on "click", ->
    showAnythingElseWrapper()

  $("#" + anythingElseN).on "click", ->
    hideAnythingElseWrapper(anythingElseText)

showAnythingElseWrapper = ->
  $("#additionalInfoWrap").slideDown 0

hideAnythingElseWrapper = (anythingElseText) ->
  $("#additionalInfoWrap").slideUp 0, -> $("#"+anythingElseText).val("")