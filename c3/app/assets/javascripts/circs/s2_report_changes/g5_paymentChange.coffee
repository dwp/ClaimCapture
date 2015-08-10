window.initEvents = (currentlyPaidIntoBankY, currentlyPaidIntoBankN, text1, text2) ->
  if not $("#" + currentlyPaidIntoBankY).prop('checked')
    hideWrap1(text1)

  if not $("#" + currentlyPaidIntoBankN).prop('checked')
    hideWrap2(text2)

  $("#" + currentlyPaidIntoBankY).on "click", ->
    hideWrap2(text2)
    showWrap1()

  $("#" + currentlyPaidIntoBankN).on "click", ->
    hideWrap1(text1)
    showWrap2()

hideWrap1 = (text1) ->
	emptyWrap1 = ->
    	$("#" + text1).val("")
  $("#currentlyPaidIntoBankWrap1").slideUp(0, emptyWrap1).attr 'aria-hidden', 'true', ->

showWrap1 = ->
  $("#currentlyPaidIntoBankWrap1").slideDown(0).attr 'aria-hidden', 'false'

hideWrap2 = (text2) ->
	emptyWrap = ->
 		$("#" + text2).val("")
  $("#currentlyPaidIntoBankWrap2").slideUp(0, emptyWrap).attr 'aria-hidden', 'true', ->

showWrap2 = ->
  $("#currentlyPaidIntoBankWrap2").slideDown(0).attr 'aria-hidden', 'false'
