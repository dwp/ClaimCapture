window.initEvents = (answerY, answerN) ->
    $("#" + answerY).on "click", ->
      $("#selfEmployedWrap").slideDown()
      $("#selfEmployedWrap").css('display', "block")

    $("#" + answerN).on "click", ->
      $("#selfEmployedWrap").slideUp()