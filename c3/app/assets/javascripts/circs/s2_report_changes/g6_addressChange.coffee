window.initEvents = (stillCaringY, stillCaringN, dateStoppedCaring,
                     caredForChangedAddress, caredForChangedAddressY, caredForChangedAddressN,
                     sameAddressY, sameAddressN, theirNewAddress, theirNewPostcode) ->
  $("#" + stillCaringN).on "click", ->
    $("#stillCaringDate").slideDown 500
    $("#stillCaringDate").css('display', "block")

    $("#stillCaringAddress").slideUp 500, ->
      $("#" + caredForChangedAddress).val("")
#    $("#"+caredForChangedAddressN).attr("checked", "")
    $("#sameAddressData").slideUp 500
    $("#changedAddressSame").slideUp 500


  $("#" + stillCaringY).on "click", ->
    $("#stillCaringDate").slideUp 500, ->
      $("#" + dateStoppedCaring).val("")

    $("#"+caredForChangedAddressN).attr("checked", false)
    $("#stillCaringAddress").slideDown 500
    $("#stillCaringAddress").css('display', "block")


  $("#" + caredForChangedAddressY).on "click", ->
    $("#changedAddressSame").slideDown 500
    $("#changedAddressSame").css('display', "block")

    #$("#sameAddressData").slideDown 500
    #$("#sameAddressData").css('display', "block")

  $("#" + caredForChangedAddressN).on "click", ->
    #$("#"+sameAddressY).attr("checked", "")
    $("#changedAddressSame").slideUp 500

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

