window.initEvents = (answerY, answerN) ->
    $("#" + answerY).on "click", ->
      $("#selfEmployedNoWrap").slideUp()

    $("#" + answerN).on "click", ->
      $("#selfEmployedNoWrap").slideDown()
      $("#selfEmployedNoWrap").css('display', "block")