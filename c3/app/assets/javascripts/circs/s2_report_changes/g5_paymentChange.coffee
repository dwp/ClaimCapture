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
  $("#currentlyPaidIntoBankWrap1").slideUp 0, ->
    $("#" + text1).val("")

showWrap1 = ->
  $("#currentlyPaidIntoBankWrap1").slideDown 0

hideWrap2 = (text2) ->
  $("#currentlyPaidIntoBankWrap2").slideUp 0, ->
  $("#" + text2).val("")

showWrap2 = ->
  $("#currentlyPaidIntoBankWrap2").slideDown 0
