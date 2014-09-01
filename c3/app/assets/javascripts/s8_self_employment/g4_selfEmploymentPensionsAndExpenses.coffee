window.initEvents = (answerY, answerN, doYouPayToLookAfterYourChildren, didYouPayToLookAfterThePersonYouCaredFor) ->

  if not $('#' + answerY).prop 'checked'
    hideSelfEmployedPensionWrap()

  $("#" + answerY).on "click", ->
    showSelfEmployedPensionWrap()

  $("#" + answerN).on "click", ->
    hideSelfEmployedPensionWrap()

  # we are returning a function here to assign it to 'conditionRequired' and which will be executed in trackSubmit.scala.html.
  return ->
    $("input[name=" + doYouPayToLookAfterYourChildren+"]:checked").val() == "no" &&
    $("input[name=" + didYouPayToLookAfterThePersonYouCaredFor+"]:checked").val() == "no"


showSelfEmployedPensionWrap = ->
  $("#selfEmployedPensionWrap").slideDown 0

hideSelfEmployedPensionWrap = ->
  $("#selfEmployedPensionWrap").slideUp 0