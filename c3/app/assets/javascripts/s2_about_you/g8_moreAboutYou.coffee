window.initEvents = (maritalStatus,hadPartnerY,hadPartnerN) ->
  $("#" + maritalStatus).on "change", ->
    if ($(this).val() == "p" || $(this).val() == "s")
      $("#hadPartnerWrapper").slideUp 500, ->
        $("#"+hadPartnerY).prop('checked', false)
        $("#"+hadPartnerN).prop('checked', false)
    else
      $("#hadPartnerWrapper").slideDown 500