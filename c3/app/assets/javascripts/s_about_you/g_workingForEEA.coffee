isChecked = (selector)  -> $("##{selector}").prop('checked')
val = (selector, text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEventsForEEA = (answer_yes, answer_no, workingForEEADetails) ->
  $("#"+workingForEEADetails).trigger("blur")
  if not isChecked(answer_yes)
    hideWorkingForEEADetailsWrapper(workingForEEADetails)

  S(answer_yes).on "click", ->
    showWorkingForEEADetailsWrapper(workingForEEADetails)

  S(answer_no).on "click", ->
    hideWorkingForEEADetailsWrapper(workingForEEADetails)

hideWorkingForEEADetailsWrapper = (workingForEEADetails) ->
  val(workingForEEADetails, "")
  S("workingForEEADetailsWrapper").slideUp(0).attr 'aria-hidden', 'true'

showWorkingForEEADetailsWrapper = (workingForEEADetails) ->
  $("#"+workingForEEADetails).trigger("blur")
  S("workingForEEADetailsWrapper").slideDown(0).attr 'aria-hidden', 'false'