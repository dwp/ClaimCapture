window.initEvents = (thirdPartyYourTheCarer, thirdPartyYourNotTheCarer, organisation) ->
  if not $("#" + thirdPartyYourNotTheCarer).prop('checked')
    hideThirdPartyWrap(organisation)

  if $("#" + thirdPartyYourNotTheCarer).prop('checked')
    showThirdPartyWrap()

  $("#" + thirdPartyYourNotTheCarer).on "click", ->
    showThirdPartyWrap()

  $("#" + thirdPartyYourTheCarer).on "click", ->
    hideThirdPartyWrap(organisation)

hideThirdPartyWrap = (organisation) ->
  emptyOrganisation = ->
    $("#" + organisation).val("")

  $("#thirdPartyWrap").slideUp(0, emptyOrganisation).attr 'aria-hidden', 'true'

showThirdPartyWrap = ->
  $("#thirdPartyWrap").slideDown(0).attr 'aria-hidden', 'false'
