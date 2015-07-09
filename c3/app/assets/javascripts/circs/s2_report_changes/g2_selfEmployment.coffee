window.initEvents = (stillCaringY, stillCaringN, whenStoppedCaring) ->
  if not $("#" + stillCaringN).prop('checked')
    hideStillCaringWrap(whenStoppedCaring)

  $("#" + stillCaringN).on "click", ->
    showStillCaringWrap()

  $("#" + stillCaringY).on "click", ->
    hideStillCaringWrap(whenStoppedCaring)

hideStillCaringWrap = (whenStoppedCaring) ->
  $("#residencyWrap").slideUp(0).attr 'aria-hidden', 'true', ->
  $("#" + whenStoppedCaring).val("")

showStillCaringWrap = ->
  $("#residencyWrap").slideDown(0).attr 'aria-hidden', 'false'
