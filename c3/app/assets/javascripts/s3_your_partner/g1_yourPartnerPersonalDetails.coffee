window.initEvents = (hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
                     ninoNi1,ninoNi2,ninoNi3,ninoNi4,ninoNi5,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
                     nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
  if not $("#" + hadPartnerY).prop('checked')
    hidePartnerDetailsWrap(hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
      ninoNi1,ninoNi2,ninoNi3,ninoNi4,ninoNi5,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
      nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN)

  $("#" + hadPartnerY).on "click", ->
    showPartnerDetailsWrap()

  $("#" + hadPartnerN).on "click", ->
    hidePartnerDetailsWrap(hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
      ninoNi1,ninoNi2,ninoNi3,ninoNi4,ninoNi5,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
      nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN)

showPartnerDetailsWrap = ->
    $("#partnerDetailsWrap").slideDown 0

hidePartnerDetailsWrap = (hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
ninoNi1,ninoNi2,ninoNi3,ninoNi4,ninoNi5,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
    $("#partnerDetailsWrap").slideUp 0, ->
      $("#" + title).val("")
      $("#" + firstName).val("")
      $("#" + middleName).val("")
      $("#" + lastName).val("")
      $("#" + otherSurName).val("")
      $("#" + ninoNi1).val("")
      $("#" + ninoNi2).val("")
      $("#" + ninoNi3).val("")
      $("#" + ninoNi4).val("")
      $("#" + ninoNi5).val("")
      $("#" + dateOfBirthDay).val("")
      $("#" + dateOfBirthMonth).val("")
      $("#" + dateOfBirthYear).val("")
      $("#" + nationality).val("")
      $("#" + seperatedFromPartnerY).prop('checked', false)
      $("#" + seperatedFromPartnerN).prop('checked', false)
      $("#" + partnerClaimingForY).prop('checked', false)
      $("#" + partnerClaimingForN).prop('checked', false)