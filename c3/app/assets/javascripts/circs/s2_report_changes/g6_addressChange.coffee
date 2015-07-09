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
  $("#stillCaringDate").slideUp(0).attr 'aria-hidden', 'true', ->
    $("#" + dateStoppedCaring).val("")

showStillCaringDate = () ->
  $("#stillCaringDate").slideDown(0).attr 'aria-hidden', 'false'

hideStillCaringAddress = (caredForChangedAddressN, caredForChangedAddressY) ->
  $("#stillCaringAddress").slideUp(0).attr 'aria-hidden', 'true'
  $("#"+caredForChangedAddressN).attr("checked", false)
  $("#"+caredForChangedAddressY).attr("checked", false)

showStillCaringAddress = () ->
  $("#stillCaringAddress").slideDown(0).attr 'aria-hidden', 'false'

hideStillCaringSameAddress = (sameAddressY, sameAddressN) ->
  $("#changedAddressSame").slideUp(0).attr 'aria-hidden', 'true'
  $("#"+sameAddressY).attr("checked", false)
  $("#"+sameAddressN).attr("checked", false)

showStillCaringSameAddress = () ->
  $("#changedAddressSame").slideDown(0).attr 'aria-hidden', 'false'

hideStillCaringSameAddressData = (theirNewAddress, theirNewPostcode) ->
  $("#sameAddressData").slideUp(0).attr 'aria-hidden', 'true'
  $("#" + theirNewAddress).val("")
  $("#" + theirNewPostcode).val("")

showStillCaringSameAddressData = () ->
  $("#sameAddressData").slideDown(0).attr 'aria-hidden', 'false'