window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->
  $("#"+anythingElseText).trigger("blur")

  if not $("#" + anythingElseY).prop 'checked'
    hideAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseY).on "click", ->
    showAnythingElseWrapper(anythingElseText)

  $("#" + anythingElseN).on "click", ->
    hideAnythingElseWrapper(anythingElseText)

showAnythingElseWrapper = (anythingElseText) ->
  $("#"+anythingElseText).trigger("blur")
  $("#additionalInfoWrap").slideDown(0).attr 'aria-hidden', 'false'

hideAnythingElseWrapper = (anythingElseText) ->
	emptyAnythingElseWrapper = ->
   		$("#additionalInfoWrap textarea").val("")
  $("#additionalInfoWrap").slideUp(0, emptyAnythingElseWrapper).attr 'aria-hidden', 'true', -> $("#"+anythingElseText).val("")