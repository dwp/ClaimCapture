window.initEvents = (answerY, answerN, answerChildrenY, answerChildrenN, answerCaredForY, answerCaredForN, isExpenseForChildrenToBeDisplayed, isExpenseForCaredForToBeDisplayed) ->
  $("#" + answerY).on "click", ->
    $("#selfEmployedPensionWrap").slideDown()
    $("#selfEmployedPensionWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#selfEmployedPensionWrap").slideUp()

  $("#" + answerChildrenY).on "click", ->
      if isExpenseForChildrenToBeDisplayed then $("#selfEmployedChildrenWrap").slideDown();$("#selfEmployedChildrenWrap").css('display', "block")

  $("#" + answerChildrenN).on "click", ->
    $("#selfEmployedChildrenWrap").slideUp()

  $("#" + answerCaredForY).on "click", ->
    if isExpenseForCaredForToBeDisplayed then $("#selfEmployedCaredForWrap").slideDown(); $("#selfEmployedCaredForWrap").css('display', "block")

  $("#" + answerCaredForN).on "click", ->
    $("#selfEmployedCaredForWrap").slideUp()