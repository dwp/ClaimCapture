window.initEvents = (answerY, answerN, text1, text2, wrap1, wrap2) ->
  console.log("passed text1:"+text1+" and text2:"+text2)
  $("#" + text1).trigger("blur")
  $("#" + text2).trigger("blur")

  if not $("#" + answerY).prop('checked')
    hideWrap1(text1,wrap1)

  if not $("#" + answerN).prop('checked')
    hideWrap2(text2,wrap2)

  $("#" + answerY).on "click", ->
    hideWrap2(text2,wrap2)
    showWrap1(wrap1)

  $("#" + answerN).on "click", ->
    hideWrap1(text1,wrap1)
    showWrap2(wrap2)

hideWrap1 = (text1,wrap1) ->
  $("#"+wrap1).slideUp(0).attr 'aria-hidden', 'true'
  $("#" + text1).val("")
  $("#" + text1).trigger("blur")

showWrap1 = (wrap1)->
  $("#"+wrap1).slideDown(0).attr 'aria-hidden', 'false'

hideWrap2 = (text2, wrap2) ->
  $("#"+wrap2).slideUp(0).attr 'aria-hidden', 'true'
  $("#" + text2).val("")
  $("#" + text2).trigger("blur")

showWrap2 = (wrap2) ->
  $("#"+wrap2).slideDown(0).attr 'aria-hidden', 'false'
