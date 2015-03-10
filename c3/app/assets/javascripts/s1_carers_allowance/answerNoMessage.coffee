window.initEvents = (id,answer_yes, answer_no) ->
  $("#" + answer_yes).on "click", ->
    $("#"+id).hide()

  $("#" + answer_no).on "click", ->
    $("#"+id).show()
    $("#"+id).css('display', "block")