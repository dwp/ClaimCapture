window.initEvents = (additionalBreaksY, additionalBreaksN, text) ->
  $("#" + additionalBreaksY).on "click", ->
    $("#additionalBreaksWrap").slideDown 500
    $("#additionalBreaksWrap").css('display', "block")

  $("#" + additionalBreaksN).on "click", ->
    $("#additionalBreaksWrap").slideUp 500, ->
      $("#" + text).val("")
