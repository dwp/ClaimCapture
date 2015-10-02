isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEventsGuardQuestion = (answer_yes, answer_no, benefitsFromEEADetails, workingForEEADetails) ->

  if not isChecked(answer_yes)
    hideQuestions(benefitsFromEEADetails,workingForEEADetails)

  S(answer_yes).on "click", ->
    showQuestions()

  S(answer_no).on "click", ->
    hideQuestions(benefitsFromEEADetails,workingForEEADetails)

hideQuestions = (benefitsFromEEADetails,workingForEEADetails) ->
  $('#eeaWrapper input:radio').prop 'checked', false
  val(workingForEEADetails, "")
  val(benefitsFromEEADetails, "")
  S("eeaWrapper").slideUp(0).attr 'aria-hidden', 'true'

showQuestions = ->
  S("eeaWrapper").slideDown(0).attr 'aria-hidden', 'false'