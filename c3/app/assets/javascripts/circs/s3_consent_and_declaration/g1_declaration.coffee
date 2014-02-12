window.initEvents = (infoAgreementY, infoAgreementN, why, check, input) ->
  selector = "#" + check
  $(selector).change ->
    if $(selector).is(':checked')
      $("#nameOrOrganisationWrapper").slideDown 500
    else
      $("#nameOrOrganisationWrapper").slideUp 500, -> $("#"+input).val("")

  $("#" + infoAgreementY).on "click", ->
    $("#why").slideUp(400, -> $("#"+why).val(""))

  $("#" + infoAgreementN).on "click", ->
    $("#why").slideDown()
    $("#why").css('display', "block")

