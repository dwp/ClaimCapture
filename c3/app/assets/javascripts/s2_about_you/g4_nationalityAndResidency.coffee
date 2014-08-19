window.initEvents = (resideInUKY, resideInUKN, residence) ->
  $("#" + resideInUKN).on "click", ->
    $("#residencyWrap").slideDown 0
    $("#residencyWrap").css('display', "block")

  $("#" + resideInUKY).on "click", ->
    $("#residencyWrap").slideUp 0, ->
      $("#" + residence).val("")
