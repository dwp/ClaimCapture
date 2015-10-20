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

window.updateNextLabel = (o) ->

	setButtonText(o)

	$("#"+o.isPersonYouCareForY).click ->
		setButtonText(o)

	$("#"+o.isPersonYouCareForN).click ->
		setButtonText(o)

	$("#"+o.hadPartnerN).click ->
		setButtonText(o)


setButtonText = (o) ->
	isPersonYouCareFor = $("#"+o.isPersonYouCareForY).is ':checked'
	isPersonYouCareForNo = $("#"+o.isPersonYouCareForN).is ':checked'
	personYouCareForValue = if isPersonYouCareFor then "yes" else "no"
	isPersonNotSelected = !isPersonYouCareFor && !isPersonYouCareForNo
	button = $("button.button")
	if !isPersonNotSelected and o.storedData != undefined and o.storedData != personYouCareForValue
		button.text(o.textNext)
	else if !isPersonNotSelected and o.storedData == undefined
		button.text(o.textNext)
	else if isPersonNotSelected and o.storedData != undefined
		button.text(o.textNext)
	else
		button.text(o.textSummary)
