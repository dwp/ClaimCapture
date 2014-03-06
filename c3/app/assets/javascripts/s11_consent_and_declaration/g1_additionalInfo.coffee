window.initEvents = (anythingElseY,anythingElseN,anythingElseText) ->
  $("#" + anythingElseY).on "click", ->
    $("#anythingElseWrapper").slideDown 500

  $("#" + anythingElseN).on "click", ->
    $("#anythingElseWrapper").slideUp 500, -> $("#"+anythingElseText).val("")