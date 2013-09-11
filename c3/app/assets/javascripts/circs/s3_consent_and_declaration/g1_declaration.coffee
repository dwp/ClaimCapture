window.initEvents = (infoAgreementY, infoAgreementN, why) ->
    $("#" + infoAgreementY).on "click", ->
      $("#why").slideUp(400, -> $("#"+why).val(""))


    $("#" + infoAgreementN).on "click", ->
      $("#why").slideDown()
      $("#why").css('display', "block")