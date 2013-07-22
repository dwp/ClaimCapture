window.initEvents = (answerY, answerN) ->
    $("#" + answerY).on "click", ->
      $("#otherPayWrap").slideDown()
      $("#otherPayWrap").css('display', "block")
    $("#" + answerN).on "click", ->
      $("#otherPayWrap").slideUp()