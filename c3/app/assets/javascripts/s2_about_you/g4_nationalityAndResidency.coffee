window.initEvents = (nationlityBritish, nationalityAnotherCountry, nationalResidency, resideInUKY, resideInUKN, resideInUKText, maritalStatusMarried, maritalStatusSingle, maritalStatusDivorced, maritalStatusWidowed, maritalStatusSeparated, maritalStatusLiving) ->
  $("#" + nationalityAnotherCountry).on "click", ->
    $("#nationalityWrap").slideDown 500
    $("#nationalityWrap").css('display', "block")

  $("#" + nationlityBritish).on "click", ->
    $("#nationalityWrap").slideUp 500, ->
      $("#" + nationalResidency).val("")
      $("#" + maritalStatusMarried).prop('checked', false)
      $("#" + maritalStatusSingle).prop('checked', false)
      $("#" + maritalStatusDivorced).prop('checked', false)
      $("#" + maritalStatusWidowed).prop('checked', false)
      $("#" + maritalStatusSeparated).prop('checked', false)
      $("#" + maritalStatusLiving).prop('checked', false)

  $("#" + resideInUKN).on "click", ->
    $("#residencyWrap").slideDown 500
    $("#residencyWrap").css('display', "block")

  $("#" + resideInUKY).on "click", ->
    $("#residencyWrap").slideUp 500, ->
      $("#" + resideInUKText).val("")
