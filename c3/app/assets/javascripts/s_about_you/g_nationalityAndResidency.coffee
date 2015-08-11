window.initEvents = (nationlityBritish, nationalityAnotherCountry, nationalResidency, resideInUKY, resideInUKN, resideInUKText) ->
  if not $("#" + nationalityAnotherCountry).prop('checked')
    hideNationalityWrap(nationalResidency)

  $("#" + nationalityAnotherCountry).on "click", ->
    showNationalityWrap()

  $("#" + nationlityBritish).on "click", ->
    hideNationalityWrap(nationalResidency)

  if not $("#" + resideInUKN).prop('checked')
    hideResidencyWrap(resideInUKText)

  $("#" + resideInUKN).on "click", ->
    showResidencyWrap()

  $("#" + resideInUKY).on "click", ->
    hideResidencyWrap(resideInUKText)


hideNationalityWrap = (nationalResidency) ->
  emptyNationalResidency = ->
    $("#" + nationalResidency).val("")

  $("#nationalityWrap").slideUp(0,emptyNationalResidency).attr 'aria-hidden', 'true'

showNationalityWrap = ->
  $("#nationalityWrap").slideDown(0).attr 'aria-hidden', 'false'

hideResidencyWrap = (resideInUKText) ->
  emptyResideInUKText = ->
    $("#" + resideInUKText).val("")

  $("#residencyWrap").slideUp(0,emptyResideInUKText).attr 'aria-hidden', 'true'

showResidencyWrap = ->
  $("#residencyWrap").slideDown(0).attr 'aria-hidden', 'false'