window.initEvents = (stillCaringY, stillCaringN, dateStoppedCaring,
                     caredForChangedAddress, caredForChangedAddressY, caredForChangedAddressN, sameAddress,
                     sameAddressY, sameAddressN, theirNewAddress, theirNewPostcode) ->
  $("#" + stillCaringN).on "click", ->
    $("#stillCaringAddress").slideUp 500, ->
      $("#" + caredForChangedAddress).val("")
    $("#sameAddressData").slideUp 500
    $("#changedAddressSame").slideUp 500

    $("#stillCaringDate").slideDown 500
    $("#stillCaringDate").css('display', "block")

  $("#" + stillCaringY).on "click", ->
    $("#stillCaringDate").slideUp 500, ->
      $("#" + dateStoppedCaring).val("")
    $("#stillCaringAddress").slideDown 500
    $("#stillCaringAddress").css('display', "block")

  $("#" + caredForChangedAddressY).on "click", ->
    $("#changedAddressSame").slideDown 500
    $("#changedAddressSame").css('display', "block")
    $("#sameAddressData").slideDown 500
    $("#sameAddressData").css('display', "block")

  $("#" + caredForChangedAddressN).on "click", ->
    $("#changedAddressSame").slideUp 500, ->
      $("#" + sameAddress).val("")
    $("#sameAddressData").slideUp 500, ->
    $("#" + theirNewAddress).val("")
    $("#" + theirNewPostcode).val("")

  $("#" + sameAddressN).on "click", ->
    $("#sameAddressData").slideDown 500
    $("#sameAddressData").css('display', "block")

  $("#" + sameAddressY).on "click", ->
    $("#sameAddressData").slideUp 500, ->
      $("#" + theirNewAddress).val("")
      $("#" + theirNewPostcode).val("")

