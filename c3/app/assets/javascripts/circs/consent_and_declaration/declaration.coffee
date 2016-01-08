window.initEvents = (infoAgreementY, infoAgreementN, why, check, input) ->
  hideWhyWrap = (why) ->
    $("#why").slideUp(0).attr 'aria-hidden', 'true'
    $("#" + why).val("")

  showWhyWrap = (input) ->
    $("#"+input).trigger("blur")
    $("#why").slideDown(0).attr 'aria-hidden', 'false'

  hideOrgWrap = (input) ->
    $("#nameOrOrganisationWrapper").slideUp(0).attr 'aria-hidden', 'true'
    $("#nameOrOrganisationWrapper input").val("")

  showOrgWrap = () ->
    $("#nameOrOrganisationWrapper").slideDown(0).attr 'aria-hidden', 'false'

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
    showWhyWrap(why)

  $("#" + infoAgreementY).on "click", ->
    hideWhyWrap(why)

  $("#" + infoAgreementN).on "click", ->
    showWhyWrap(why)



