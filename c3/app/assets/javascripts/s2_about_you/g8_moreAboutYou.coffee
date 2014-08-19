window.initEvents = (maritalStatus,hadPartnerY,hadPartnerN,hadPartner,message1,message2) ->
  $("#" + maritalStatus + " input").on "change", ->
    if ($(this).val() != "w")
      $("#" + hadPartner).prev().html(message1)
    else
      $("#" + hadPartner).prev().html(message2)

    if ($(this).val() == "p")
      $("#hadPartnerWrapper").slideUp 0, ->
        $("#"+hadPartnerY).prop('checked', false)
        $("#"+hadPartnerN).prop('checked', false)
    else if($(this).val() != "")
      $("#hadPartnerWrapper").slideDown 0