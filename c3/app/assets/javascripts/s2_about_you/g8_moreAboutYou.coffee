window.initEvents = (maritalStatus,hadPartnerY,hadPartnerN,hadPartner,message1,message2) ->

  if not ($("#" + maritalStatus + " input").prop('checked'))
    hideHadPartnerWrapper(hadPartnerY,hadPartnerN)

  $("#" + maritalStatus + " input").on "change", ->
    if ($(this).val() != "w")
      $("#" + hadPartner).prev().html(message1)
    else
      $("#" + hadPartner).prev().html(message2)

    if ($(this).val() == "p")
      hideHadPartnerWrapper(hadPartnerY,hadPartnerN)
    else if($(this).val() != "")
      showHadPartnerWrapper()


showHadPartnerWrapper = ->
  $("#hadPartnerWrapper").slideDown 0

hideHadPartnerWrapper = (hadPartnerY,hadPartnerN) ->
  $("#hadPartnerWrapper").slideUp 0, ->
    $("#"+hadPartnerY).prop('checked', false)
    $("#"+hadPartnerN).prop('checked', false)