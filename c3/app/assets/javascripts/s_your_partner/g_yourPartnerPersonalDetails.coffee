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

window.updateNextLabel = (isPersonYouCareForY,isPersonYouCareForN,storedData,textNext,textSummary) ->

	setButtonText(isPersonYouCareForY,isPersonYouCareForN,storedData,textNext,textSummary)

	$("#"+isPersonYouCareForY).click ->
		setButtonText(isPersonYouCareForY,isPersonYouCareForN,storedData,textNext,textSummary)

	$("#"+isPersonYouCareForN).click ->
		setButtonText(isPersonYouCareForY,isPersonYouCareForN,storedData,textNext,textSummary)


setButtonText = (isPersonYouCareForY,isPersonYouCareForN,storedData,textNext,textSummary) ->
	isPersonYouCareFor = $("#"+isPersonYouCareForY).is ':checked'
	isPersonYouCareForNo = $("#"+isPersonYouCareForN).is ':checked'
	personYouCareForValue = if isPersonYouCareFor then "yes" else "no"
	isPersonNotSelected = !isPersonYouCareFor && !isPersonYouCareForNo
	button = $("button.button")
	if !isPersonNotSelected and storedData != undefined and storedData != personYouCareForValue
		button.text(textNext)
	else if !isPersonNotSelected and storedData == undefined
		button.text(textNext)
	else if isPersonNotSelected and storedData != undefined
		button.text(textNext)
	else
		button.text(textSummary)


#case (Some(data),Some(isPartnerPerson))
#		if data.isPartnerPersonYouCareFor.nonEmpty && data.isPartnerPersonYouCareFor.get != isPartnerPerson => false
#		case (Some(data),None)
#		if data.isPartnerPersonYouCareFor.nonEmpty => false
#		case (None,Some(_)) => false