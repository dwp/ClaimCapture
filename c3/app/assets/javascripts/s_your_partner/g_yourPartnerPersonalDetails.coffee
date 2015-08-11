window.initEvents = (hadPartnerY,hadPartnerN,title,titleMr,titleMrs,titleMiss,titleMs,titleOther,titleOtherText,firstName,middleName,lastName,otherSurName,
                     nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
                     nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
  if not $("#" + hadPartnerY).prop('checked')
    hidePartnerDetailsWrap(hadPartnerY,hadPartnerN,title,titleMr,titleMrs,titleMiss,titleMs,titleOther,titleOtherText,firstName,middleName,lastName,otherSurName,
      nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
      nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN)

  $("#" + hadPartnerY).on "click", ->
    showPartnerDetailsWrap()

  $("#" + hadPartnerN).on "click", ->
    hidePartnerDetailsWrap(hadPartnerY,hadPartnerN,title,titleMr,titleMrs,titleMiss,titleMs,titleOther,titleOtherText,firstName,middleName,lastName,otherSurName,
      nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
      nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN)

hidePartnerDetailsWrap = (hadPartnerY,hadPartnerN,title,titleMr,titleMrs,titleMiss,titleMs,titleOther,titleOtherText,firstName,middleName,lastName,otherSurName,
nino,dateOfBirthDay,dateOfBirthMonth,dateOfBirthYear,
nationality,seperatedFromPartnerY,seperatedFromPartnerN,partnerClaimingForY,partnerClaimingForN) ->
	emptyPartnerDetails = ->
		$("#" + titleMr).prop('checked', false)
		$("#" + titleMrs).prop('checked', false)
		$("#" + titleMiss).prop('checked', false)
		$("#" + titleMs).prop('checked', false)
		$("#" + titleOther).prop('checked', false)
		$("#" + titleOtherText).val("")
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
	$("#partnerDetailsWrap").slideUp(0,emptyPartnerDetails).attr 'aria-hidden', 'true', ->

showPartnerDetailsWrap = ->
	$("#partnerDetailsWrap").slideDown(0).attr 'aria-hidden', 'false'