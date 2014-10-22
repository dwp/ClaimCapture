window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->

  if not $("#" + anythingElseY).prop 'checked'
    hideAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseY).on "click", ->
    showAnythingElseWrapper()

  $("#" + anythingElseN).on "click", ->
    hideAnythingElseWrapper(anythingElseText)

showAnythingElseWrapper = ->
  $("#anythingElseWrapper").slideDown 0

hideAnythingElseWrapper = (anythingElseText) ->
  $("#anythingElseWrapper").slideUp 0, -> $("#"+anythingElseText).val("")