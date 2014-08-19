window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->
  $("#" + anythingElseY).on "click", ->
    $("#anythingElseWrapper").slideDown 0

  $("#" + anythingElseN).on "click", ->
    $("#anythingElseWrapper").slideUp 0, -> $("#"+anythingElseText).val("")