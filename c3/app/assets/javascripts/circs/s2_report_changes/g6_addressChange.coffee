window.initEvents = (stillCaringY, stillCaringN, dateStoppedCaring,
                     caredForChangedAddress, caredForChangedAddressY, caredForChangedAddressN,
                     sameAddressY, sameAddressN, theirNewAddress, theirNewPostcode) ->
  if not $("#" + stillCaringN).prop("checked")
    hideStillCaringDate(dateStoppedCaring)

  if not $("#" + stillCaringY).prop("checked")
    hideStillCaringAddress(caredForChangedAddressN, caredForChangedAddressY)
    hideStillCaringSameAddress(sameAddressY, sameAddressN)
    hideStillCaringSameAddressData(theirNewAddress, theirNewPostcode)

  if not $("#" + caredForChangedAddressY).prop("checked")
    hideStillCaringSameAddress(sameAddressY, sameAddressN)
    hideStillCaringSameAddressData(theirNewAddress, theirNewPostcode)

  if not $("#" + sameAddressN).prop("checked")
    hideStillCaringSameAddressData(theirNewAddress, theirNewPostcode)

  $("#" + stillCaringN).on "click", ->
    showStillCaringDate()
    hideStillCaringAddress(caredForChangedAddressN, caredForChangedAddressY)
    hideStillCaringSameAddress(sameAddressY, sameAddressN)
    hideStillCaringSameAddressData(theirNewAddress, theirNewPostcode)

  $("#" + stillCaringY).on "click", ->
    hideStillCaringDate(dateStoppedCaring)
    showStillCaringAddress()

  $("#" + caredForChangedAddressY).on "click", ->
    showStillCaringSameAddress()

  $("#" + caredForChangedAddressN).on "click", ->
    hideStillCaringSameAddress(sameAddressY, sameAddressN)
    hideStillCaringSameAddressData(theirNewAddress, theirNewPostcode)

  $("#" + sameAddressN).on "click", ->
    showStillCaringSameAddressData()

  $("#" + sameAddressY).on "click", ->
    hideStillCaringSameAddressData(theirNewAddress, theirNewPostcode)


hideStillCaringDate = (dateStoppedCaring) ->
	emptyStillCaringDate = ->
    	$("#" + dateStoppedCaring).val("")
  $("#stillCaringDate").slideUp(0, emptyStillCaringDate).attr 'aria-hidden', 'true', ->

showStillCaringDate = () ->
  $("#stillCaringDate").slideDown(0).attr 'aria-hidden', 'false'

hideStillCaringAddress = (caredForChangedAddressN, caredForChangedAddressY) ->
	emptyStillCaringAddress = ->
		$("#"+caredForChangedAddressN).attr("checked", false)
		$("#"+caredForChangedAddressY).attr("checked", false)
  $("#stillCaringAddress").slideUp(0, emptyStillCaringAddress).attr 'aria-hidden', 'true'

showStillCaringAddress = () ->
  $("#stillCaringAddress").slideDown(0).attr 'aria-hidden', 'false'

hideStillCaringSameAddress = (sameAddressY, sameAddressN) ->
	emptyStillCaringSameAddres = ->
		$("#"+sameAddressY).attr("checked", false)
		$("#"+sameAddressN).attr("checked", false)
	$("#changedAddressSame").slideUp(0, emptyStillCaringSameAddres).attr 'aria-hidden', 'true'

showStillCaringSameAddress = () ->
  $("#changedAddressSame").slideDown(0).attr 'aria-hidden', 'false'

hideStillCaringSameAddressData = (theirNewAddress, theirNewPostcode) ->
	emptyStillCaringSameAddressData = ->
		$("#" + theirNewAddress).val("")
		$("#" + theirNewPostcode).val("")
  $("#sameAddressData").slideUp(0, emptyStillCaringSameAddressData).attr 'aria-hidden', 'true'

showStillCaringSameAddressData = () ->
  $("#sameAddressData").slideDown(0).attr 'aria-hidden', 'false'