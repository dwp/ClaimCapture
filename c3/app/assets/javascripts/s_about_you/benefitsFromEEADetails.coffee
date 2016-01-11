isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEventsFromEEA = (answer_yes, answer_no, benefitsFromEEADetails) ->
  $("#"+benefitsFromEEADetails).trigger("blur")
  if not isChecked(answer_yes)
    hideBenefitsFromEEADetailsWrapper(benefitsFromEEADetails)

  S(answer_yes).on "click", ->
    showBenefitsFromEEADetailsWrapper(benefitsFromEEADetails)

  S(answer_no).on "click", ->
    hideBenefitsFromEEADetailsWrapper(benefitsFromEEADetails)

hideBenefitsFromEEADetailsWrapper = (benefitsFromEEADetails) ->
  val(benefitsFromEEADetails,"")
  S("benefitsFromEEADetailsWrapper").slideUp(0).attr 'aria-hidden', 'true'

showBenefitsFromEEADetailsWrapper = (benefitsFromEEADetails) ->
  $("#"+benefitsFromEEADetails).trigger("blur")
  S("benefitsFromEEADetailsWrapper").slideDown(0).attr 'aria-hidden', 'false'