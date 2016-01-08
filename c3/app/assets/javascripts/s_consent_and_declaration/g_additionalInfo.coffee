window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->

  if not $("#" + anythingElseY).prop 'checked'
    hideAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseY).on "click", ->
    showAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseN).on "click", ->
    hideAnythingElseWrapper(anythingElseText)

showAnythingElseWrapper = (anythingElseText) ->
  $("#"+anythingElseText).trigger("blur")
  $("#anythingElseWrapper").slideDown(0).attr 'aria-hidden', 'false'

hideAnythingElseWrapper = (anythingElseText) ->
	emptyAnythingElseWrapper = ->
   		$("#"+anythingElseText).val("")
  $("#anythingElseWrapper").slideUp(0, emptyAnythingElseWrapper).attr 'aria-hidden', 'true', -> 