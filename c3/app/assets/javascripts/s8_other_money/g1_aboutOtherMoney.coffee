window.initEvents = (answerY, answerN, eitherClaimedBenefitSinceClaimDate) ->
    $("#" + answerY).on "click", ->
      $("#yourBenefitsText1Wrap").slideDown()
      $("#yourBenefitsText1Wrap").css('display', "block")
      if eitherClaimedBenefitSinceClaimDate then $("#yourBenefitsText2Wrap").slideDown()
      if eitherClaimedBenefitSinceClaimDate then $("#yourBenefitsText2Wrap").css('display', "block")

    $("#" + answerN).on "click", ->
      $("#yourBenefitsText1Wrap").slideUp()
      if eitherClaimedBenefitSinceClaimDate then $("#yourBenefitsText2Wrap").slideUp()