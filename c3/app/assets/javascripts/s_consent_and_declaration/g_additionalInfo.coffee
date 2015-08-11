window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->

  if not $("#" + anythingElseY).prop 'checked'
    hideAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseY).on "click", ->
    showAnythingElseWrapper()

  $("#" + anythingElseN).on "click", ->
    hideAnythingElseWrapper(anythingElseText)

showAnythingElseWrapper = ->
  $("#anythingElseWrapper").slideDown(0).attr 'aria-hidden', 'false'

hideAnythingElseWrapper = (anythingElseText) ->
	emptyAnythingElseWrapper = ->
   		$("#"+anythingElseText).val("")
  $("#anythingElseWrapper").slideUp(0, emptyAnythingElseWrapper).attr 'aria-hidden', 'true', -> 