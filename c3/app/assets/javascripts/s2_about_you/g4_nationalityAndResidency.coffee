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
  $("#nationalityWrap").slideUp 0, ->
    $("#" + nationalResidency).val("")


showNationalityWrap = ->
  $("#nationalityWrap").slideDown 0

hideResidencyWrap = (resideInUKText) ->
  $("#residencyWrap").slideUp 0, ->
  $("#" + resideInUKText).val("")

showResidencyWrap = ->
  $("#residencyWrap").slideDown 0
