isChecked = (selector)  -> $("##{selector}").prop('checked')
val = (selector, text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents = (you, dp, youEnded_yes, youEnded_no, dpEnded_yes, dpEnded_no,
  youEndedWrapYes, youEndedWrapNo, youAdmittedWrap, dpEndedWrapYes, dpEndedWrapNo, dpAdmittedWrap, medicalProfession, present, past,
                     expectToCareAgain_yes, expectToCareAgain_no, expectToCareAgain_dontknow, expectYesWrap, expectNoWrap, dontknowWrap,
                     expectToCareAgain2_yes, expectToCareAgain2_no, expectToCareAgain2_dontknow, expectYesWrap2, expectNoWrap2, dontknowWrap2
  ) ->
  if not isChecked(you)
    hideWrapper(youAdmittedWrap)

  if not isChecked(dp)
    hideWrapper(dpAdmittedWrap)

  if not isChecked(youEnded_yes)
    hideWrapper(youEndedWrapYes)

  if not isChecked(youEnded_no)
    hideWrapper(youEndedWrapNo)

  if not isChecked(dpEnded_yes)
    hideWrapper(dpEndedWrapYes)

  if not isChecked(dpEnded_no)
    hideWrapper(dpEndedWrapNo)

  if not isChecked(expectToCareAgain_yes)
    hideWrapper(expectYesWrap)

  if not isChecked(expectToCareAgain_no)
    hideWrapper(expectNoWrap)

  if not isChecked(expectToCareAgain_dontknow)
    hideWrapper(dontknowWrap)

  if not isChecked(expectToCareAgain2_yes)
    hideWrapper(expectYesWrap2)

  if not isChecked(expectToCareAgain2_no)
    hideWrapper(expectNoWrap2)

  if not isChecked(expectToCareAgain2_dontknow)
    hideWrapper(dontknowWrap2)

  S(you).on "click", ->
    hideWrapper(dpAdmittedWrap)
    showWrapper(youAdmittedWrap)

  S(dp).on "click", ->
    hideWrapper(youAdmittedWrap)
    showWrapper(dpAdmittedWrap)

  S(youEnded_yes).on "click", ->
    updateYourProfessionLabel(youEnded_yes, youEnded_no, medicalProfession, present, past)
    showWrapper(youEndedWrapYes)
    hideWrapper(youEndedWrapNo)

  S(dpEnded_yes).on "click", ->
    showWrapper(dpEndedWrapYes)
    hideWrapper(dpEndedWrapNo)

  S(dpEnded_no).on "click", ->
    showWrapper(dpEndedWrapNo)
    hideWrapper(dpEndedWrapYes)

  S(youEnded_no).on "click", ->
    updateYourProfessionLabel(youEnded_yes, youEnded_no, medicalProfession, present, past)
    hideWrapper(youEndedWrapYes)
    showWrapper(youEndedWrapNo)

  S(expectToCareAgain_yes).on "click", ->
    hideWrapper(expectNoWrap)
    hideWrapper(dontknowWrap)
    showWrapper(expectYesWrap)

  S(expectToCareAgain_no).on "click", ->
    hideWrapper(expectYesWrap)
    hideWrapper(dontknowWrap)
    showWrapper(expectNoWrap)

  S(expectToCareAgain_dontknow).on "click", ->
    hideWrapper(expectYesWrap)
    hideWrapper(expectNoWrap)
    showWrapper(dontknowWrap)

  S(expectToCareAgain2_yes).on "click", ->
    hideWrapper(expectNoWrap2)
    hideWrapper(dontknowWrap2)
    showWrapper(expectYesWrap2)

  S(expectToCareAgain2_no).on "click", ->
    hideWrapper(expectYesWrap2)
    hideWrapper(dontknowWrap2)
    showWrapper(expectNoWrap2)

  S(expectToCareAgain2_dontknow).on "click", ->
    hideWrapper(expectYesWrap2)
    hideWrapper(expectNoWrap2)
    showWrapper(dontknowWrap2)

showWrapper = (wrapper) ->
  $("#" + wrapper).slideDown(0).attr 'aria-hidden', 'false'

hideWrapper = (wrapper)->
  clearDownStreamInputs(wrapper)
  $("#" + wrapper).slideUp(0).attr 'aria-hidden', 'true'

clearDownStreamInputs = (wrapper)->
  $("#" + wrapper).find("input").each(clearInput)
  $("#" + wrapper).find("textarea").each(clearInput)

# If we want to also clear the validation error when item is hidden ?
# $("#" + wrapper).find(".validation-error").removeClass("validation-error")
# $("#" + wrapper).find(".validation-message").remove()
clearInput = ->
  if( $(this).attr("type") == "radio" )
    $(this).prop('checked', false)
    $(this).parent().removeClass("selected")
  else
    $(this).val("")

updateYourProfessionLabel = (youEnded_yes, youEnded_no, medicalProfession, present, past) ->
  label = S(medicalProfession).prev()
  if isChecked(youEnded_yes)
    if label
      label.text(past).append(label.children())
  else
    if label
      label.text(present).append(label.children())
