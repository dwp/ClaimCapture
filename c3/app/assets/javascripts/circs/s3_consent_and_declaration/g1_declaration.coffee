window.initEvents = (infoAgreementY, infoAgreementN, why, check, input) ->
  hideWhyWrap = (why) ->
    $("#why").slideUp 0, ->
      $("#" + why).val("")

  showWhyWrap = () ->
    $("#why").slideDown 0

  hideOrgWrap = (input) ->
    $("#nameOrOrganisationWrapper").slideUp 0, ->
      $("#" + input).val("")

  showOrgWrap = () ->
    $("#nameOrOrganisationWrapper").slideDown 0

  selector = "#" + check
  if not $(selector).is(':checked')
    hideOrgWrap(input)

  if $(selector).is(':checked')
    showOrgWrap()

  $(selector).change ->
    if $(selector).is(':checked')
      showOrgWrap()
    else
      hideOrgWrap(input)

  if not $("#" + infoAgreementN).prop('checked')
    hideWhyWrap(why)

  if $("#" + infoAgreementN).prop('checked')
    showWhyWrap()

  $("#" + infoAgreementY).on "click", ->
    hideWhyWrap(why)

  $("#" + infoAgreementN).on "click", ->
    showWhyWrap()



