window.initEvents = (answerY, answerN) ->
    $("#" + answerY).on "click", ->
      $("#yourBenefitsTextWrap").slideDown()
      $("#yourBenefitsTextWrap").css('display', "block")

    $("#" + answerN).on "click", ->
      $("#yourBenefitsTextWrap").slideUp()