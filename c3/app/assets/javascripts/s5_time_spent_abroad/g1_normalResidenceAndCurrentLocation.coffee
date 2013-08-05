window.initEvents = (answerY, answerN) ->
    $("#" + answerN).on "click", ->
      $("#liveInUK").slideDown()
      $("#liveInUK").css('display', "block")

    $("#" + answerY).on "click", ->
      $("#liveInUK").slideUp()