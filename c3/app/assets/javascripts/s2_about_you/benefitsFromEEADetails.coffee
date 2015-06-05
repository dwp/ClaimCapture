isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents = (answer_yes, answer_no, benefitsFromEEADetails) ->

  if not isChecked(answer_yes)
    hideBenefitsFromEEADetailsWrapper(benefitsFromEEADetails)

  S(answer_yes).on "click", ->
    showBenefitsFromEEADetailsWrapper()

  S(answer_no).on "click", ->
    hideBenefitsFromEEADetailsWrapper(benefitsFromEEADetails)

hideBenefitsFromEEADetailsWrapper = (benefitsFromEEADetails) ->
  val(benefitsFromEEADetails,"")
  S("benefitsFromEEADetailsWrapper").slideUp 0

showBenefitsFromEEADetailsWrapper = ->
  S("benefitsFromEEADetailsWrapper").slideDown 0