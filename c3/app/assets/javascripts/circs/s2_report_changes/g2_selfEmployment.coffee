window.initEvents = (stillCaringY, stillCaringN, whenStoppedCaring) ->
  $("#" + stillCaringN).on "click", ->
    $("#stillCaringWrap").slideDown 500
    $("#stillCaringWrap").css('display', "block")

  $("#" + stillCaringY).on "click", ->
    $("#stillCaringWrap").slideUp 500, ->
      $("#" + whenStoppedCaring).val("")
