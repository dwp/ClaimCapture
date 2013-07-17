window.initEvents = (answerY, answerN) ->
    $("#" + answerY).on "click", ->
      $("#yourBenefitsWrap").slideDown()
      $("#yourBenefitsWrap").css('display', "block")

    $("#" + answerN).on "click", ->
      $("#yourBenefitsWrap").slideUp()