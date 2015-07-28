isChecked = (selector)  -> $("##{selector}").prop('checked')
val = (selector, text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEventsForEEA = (answer_yes, answer_no, workingForEEADetails) ->
  if not isChecked(answer_yes)
    hideWorkingForEEADetailsWrapper(workingForEEADetails)

  S(answer_yes).on "click", ->
    showWorkingForEEADetailsWrapper()

  S(answer_no).on "click", ->
    hideWorkingForEEADetailsWrapper(workingForEEADetails)

hideWorkingForEEADetailsWrapper = (workingDetails) ->
  val(workingDetails, "")
  S("workingForEEADetailsWrapper").slideUp(0).attr 'aria-hidden', 'true'

showWorkingForEEADetailsWrapper = ->
  S("workingForEEADetailsWrapper").slideDown(0).attr 'aria-hidden', 'false'