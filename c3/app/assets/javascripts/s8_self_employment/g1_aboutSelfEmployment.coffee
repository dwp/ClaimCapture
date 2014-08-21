window.initEvents = (answerY, answerN) ->
    $("#" + answerY).on "click", ->
      $("#selfEmployedNoWrap").slideUp 0

    $("#" + answerN).on "click", ->
      $("#selfEmployedNoWrap").slideDown 0
      $("#selfEmployedNoWrap").css('display', "block")