window.initEvents = (resideInUKY, resideInUKN, residence) ->
  $("#" + resideInUKN).on "click", ->
    $("#residencyWrap").slideDown 500
    $("#residencyWrap").css('display', "block")

  $("#" + resideInUKY).on "click", ->
    $("#residencyWrap").slideUp 500, ->
      $("#" + residence).val("")
