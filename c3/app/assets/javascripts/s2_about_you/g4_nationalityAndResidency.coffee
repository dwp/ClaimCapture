window.initEvents = (nationlityBritish, nationalityAnotherCountry, nationalResidency, resideInUKY, resideInUKN, resideInUKText, maritalStatusMarried, maritalStatusSingle, maritalStatusDivorced, maritalStatusWidowed, maritalStatusSeparated, maritalStatusLiving) ->
  if not $("#" + nationalityAnotherCountry).prop('checked')
    hideNationalityWrap(nationalResidency, maritalStatusMarried, maritalStatusSingle, maritalStatusDivorced, maritalStatusWidowed, maritalStatusSeparated, maritalStatusLiving)

  $("#" + nationalityAnotherCountry).on "click", ->
    showNationalityWrap()

  $("#" + nationlityBritish).on "click", ->
    hideNationalityWrap(nationalResidency, maritalStatusMarried, maritalStatusSingle, maritalStatusDivorced, maritalStatusWidowed, maritalStatusSeparated, maritalStatusLiving)

  if not $("#" + resideInUKN).prop('checked')
    hideResidencyWrap(resideInUKText)

  $("#" + resideInUKN).on "click", ->
    showResidencyWrap()

  $("#" + resideInUKY).on "click", ->
    hideResidencyWrap(resideInUKText)


hideNationalityWrap = (nationalResidency, maritalStatusMarried, maritalStatusSingle, maritalStatusDivorced, maritalStatusWidowed, maritalStatusSeparated, maritalStatusLiving) ->
  $("#nationalityWrap").slideUp 0, ->
    $("#" + nationalResidency).val("")
    $("#" + maritalStatusMarried).prop('checked', false)
    $("#" + maritalStatusSingle).prop('checked', false)
    $("#" + maritalStatusDivorced).prop('checked', false)
    $("#" + maritalStatusWidowed).prop('checked', false)
    $("#" + maritalStatusSeparated).prop('checked', false)
    $("#" + maritalStatusLiving).prop('checked', false)
    $("#nationalityWrap").css('display', "none")

showNationalityWrap = ->
  $("#nationalityWrap").slideDown 0
  $("#nationalityWrap").css('display', "block")

hideResidencyWrap = (resideInUKText) ->
  $("#residencyWrap").slideUp 0, ->
  $("#" + resideInUKText).val("")
  $("#residencyWrap").css('display', "none")

showResidencyWrap = ->
  $("#residencyWrap").slideDown 0
  $("#residencyWrap").css('display', "block")
