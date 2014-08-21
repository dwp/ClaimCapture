window.initEvents = (nationlityBritish, nationalityAnotherCountry, nationalResidency, resideInUKY, resideInUKN, resideInUKText, maritalStatusMarried, maritalStatusSingle, maritalStatusDivorced, maritalStatusWidowed, maritalStatusSeparated, maritalStatusLiving) ->
  $("#" + nationalityAnotherCountry).on "click", ->
    $("#nationalityWrap").slideDown 0
    $("#nationalityWrap").css('display', "block")

  $("#" + nationlityBritish).on "click", ->
    $("#nationalityWrap").slideUp 0, ->
      $("#" + nationalResidency).val("")
      $("#" + maritalStatusMarried).prop('checked', false)
      $("#" + maritalStatusSingle).prop('checked', false)
      $("#" + maritalStatusDivorced).prop('checked', false)
      $("#" + maritalStatusWidowed).prop('checked', false)
      $("#" + maritalStatusSeparated).prop('checked', false)
      $("#" + maritalStatusLiving).prop('checked', false)

  $("#" + resideInUKN).on "click", ->
    $("#residencyWrap").slideDown 0
    $("#residencyWrap").css('display', "block")

  $("#" + resideInUKY).on "click", ->
    $("#residencyWrap").slideUp 0, ->
      $("#" + resideInUKText).val("")
