window.initEvents = (anythingElseY, anythingElseN, anythingElse) ->
  $("#" + anythingElseY).on "click", ->
    $("#anythingElseWrapper").slideDown 500
    $("#anythingElseWrapper").css('display', "block")


  $("#" + anythingElseN).on "click", ->
    $("#anythingElseWrapper").slideUp 500, ->
      $("#" + anythingElse).val("")

