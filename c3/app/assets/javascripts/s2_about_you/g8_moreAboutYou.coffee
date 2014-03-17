window.initEvents = (maritalStatus,hadPartnerY,hadPartnerN) ->
  $("#" + maritalStatus + " input").on "change", ->
    if ($(this).val() == "p")
      $("#hadPartnerWrapper").slideUp 500, ->
        $("#"+hadPartnerY).prop('checked', false)
        $("#"+hadPartnerN).prop('checked', false)
    else if($(this).val() != "")
      $("#hadPartnerWrapper").slideDown 500