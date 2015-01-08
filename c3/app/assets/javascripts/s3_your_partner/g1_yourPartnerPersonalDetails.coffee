window.initEvents = (hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
                     nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
                     nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
  if not $("#" + hadPartnerY).prop('checked')
    hidePartnerDetailsWrap(hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
      nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
      nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN)

  $("#" + hadPartnerY).on "click", ->
    showPartnerDetailsWrap()

  $("#" + hadPartnerN).on "click", ->
    hidePartnerDetailsWrap(hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
      nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
      nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN)

showPartnerDetailsWrap = ->
    $("#partnerDetailsWrap").slideDown 0

hidePartnerDetailsWrap = (hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
    $("#partnerDetailsWrap").slideUp 0, ->
      $("#" + title).val("")
      $("#" + firstName).val("")
      $("#" + middleName).val("")
      $("#" + lastName).val("")
      $("#" + otherSurName).val("")
      $("#" + nino).val("")
      $("#" + dateOfBirthDay).val("")
      $("#" + dateOfBirthMonth).val("")
      $("#" + dateOfBirthYear).val("")
      $("#" + nationality).val("")
      $("#" + seperatedFromPartnerY).prop('checked', false)
      $("#" + seperatedFromPartnerN).prop('checked', false)
      $("#" + partnerClaimingForY).prop('checked', false)
      $("#" + partnerClaimingForN).prop('checked', false)