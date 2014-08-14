window.initEvents = (nationlityBritish, nationalityAnotherCountry, nationalResidency, resideInUKY, resideInUKN, resideInUKText) ->
  $("#" + nationalityAnotherCountry).on "click", ->
    $("#nationalityWrap").slideDown 500
    $("#nationalityWrap").css('display', "block")

  $("#" + nationlityBritish).on "click", ->
    $("#nationalityWrap").slideUp 500, ->
      $("#" + nationalResidency).val("")

  $("#" + resideInUKN).on "click", ->
    $("#residencyWrap").slideDown 500
    $("#residencyWrap").css('display', "block")

  $("#" + resideInUKY).on "click", ->
    $("#residencyWrap").slideUp 500, ->
      $("#" + resideInUKText).val("")
