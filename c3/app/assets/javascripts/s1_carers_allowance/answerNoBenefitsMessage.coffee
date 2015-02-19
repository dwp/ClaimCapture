benefitsSelector = (selector) -> $("##{selector} input")

window.initEvents = (benefitsAnswerId, nobValue) ->
  benefitsSelector(benefitsAnswerId).on "change", ->
    if $(this).val() == nobValue
      $("#answerNoMessageWrap").show()
      $("#answerNoMessageWrap").css('display', "block")
    else
      $("#answerNoMessageWrap").hide()