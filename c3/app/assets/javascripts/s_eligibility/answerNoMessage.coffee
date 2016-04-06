window.initEvents = (id,answer_yes, answer_no) ->
  $("#" + answer_yes).on "click", ->
    $("#"+id).hide()

  $("#" + answer_no).on "click", ->
    $("#"+id).show()
    $("#"+id).css('display', "block")

window.originWarning = (answer1, answer2, answer3, warning) ->
  if not $("#" + answer3).prop('checked')
    $("#"+warning).hide()

  $("#" + answer1).on "click", ->
    $("#"+warning).hide()

  $("#" + answer2).on "click", ->
    $("#"+warning).hide()

  $("#" + answer3).on "click", ->
    $("#"+warning).show()