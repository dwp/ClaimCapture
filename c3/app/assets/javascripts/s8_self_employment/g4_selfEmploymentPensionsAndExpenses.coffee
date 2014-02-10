window.initEvents = (answerY, answerN, doYouPayToLookAfterYourChildren, didYouPayToLookAfterThePersonYouCaredFor) ->
  $("#" + answerY).on "click", ->
    $("#selfEmployedPensionWrap").slideDown()
    $("#selfEmployedPensionWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#selfEmployedPensionWrap").slideUp()

  return ->
    $("input[name=" + doYouPayToLookAfterYourChildren+"]:checked").val() == "no" &&
    $("input[name=" + didYouPayToLookAfterThePersonYouCaredFor+"]:checked").val() == "no"