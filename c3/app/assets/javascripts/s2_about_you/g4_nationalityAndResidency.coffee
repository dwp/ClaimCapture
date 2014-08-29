window.initEvents = (resideInUKY, resideInUKN, residence) ->

  if not $("#" + resideInUKN).prop('checked')
    hideResidencyWrap(residence)

  $("#" + resideInUKN).on "click", ->
    showResidencyWrap()


  $("#" + resideInUKY).on "click", ->
    hideResidencyWrap(residence)


showResidencyWrap = ->
  $("#residencyWrap").slideDown 0

hideResidencyWrap = (residence) ->
  $("#residencyWrap").slideUp 0, ->
    $("#" + residence).val("")
