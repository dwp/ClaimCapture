window.initEvents = (answerY, answerN) ->

  if not $("#"+answerN).prop 'checked'
    hideSelfEmployedNoWrap()

  $("#" + answerY).on "click", ->
    hideSelfEmployedNoWrap()

  $("#" + answerN).on "click", ->
    showSelfEmployedNoWrap()


showSelfEmployedNoWrap = ->
  $("#selfEmployedNoWrap").slideDown(0).attr 'aria-hidden', 'false'

hideSelfEmployedNoWrap = ->
	emptySelfEmployedNoWrap = ->
	    $("#selfEmployedNoWrap input:not([type='radio']").val("")
	    $("#selfEmployedNoWrap input[type='radio']").prop("checked", false)
  $("#selfEmployedNoWrap").slideUp(0, emptySelfEmployedNoWrap).attr 'aria-hidden', 'true'
