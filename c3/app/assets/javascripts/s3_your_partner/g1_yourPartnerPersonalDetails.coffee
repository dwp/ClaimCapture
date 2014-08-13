window.initEvents = (hadPartnerY,hadPartnerN,title,firstName,middleName,lastName,otherSurName,
                     ninoNi1,ninoNi2,ninoNi3,ninoNi4,ninoNi5,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
                     nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
  $("#" + hadPartnerY).on "click", ->
    $("#partnerDetailsWrap").slideDown 500
    $("#partnerDetailsWrap").css('display', "block")

  $("#" + hadPartnerN).on "click", ->
    $("#partnerDetailsWrap").slideUp 500, ->
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


