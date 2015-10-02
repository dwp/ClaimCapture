window.updateNextLabel = (employment_yes,employment_no,selfEmployment_yes,selfEmployment_no,previousEmployed,previousSelfEmployed,textNext,textReturn) ->
  changeText = ->
    button = $("button.button")
    if goToPreview()
      button.text(textReturn)
    else
      button.text(textNext)

  goToPreview = ->
    bPreviousEmployed = if previousEmployed == "yes" then true else false
    bPreviousSelfEmployed = if previousSelfEmployed == "yes" then true else false
    beenEmployed = $("#"+employment_yes).is ":checked"
    beenSelfEmployed = $("#"+selfEmployment_yes).is ":checked"
    employmentHasChanged = beenEmployed != bPreviousEmployed
    selfEmploymentHasChanged = beenSelfEmployed != bPreviousSelfEmployed
    bothHaveNotChanged = !employmentHasChanged && !selfEmploymentHasChanged
    selfENotChangedAndEmploymentNo = bPreviousEmployed && !beenEmployed && !selfEmploymentHasChanged
    empNotChangedAndSENo = bPreviousSelfEmployed && !beenSelfEmployed && !employmentHasChanged
    bothAnswersAreNo = !beenEmployed && !beenSelfEmployed

    result = bothAnswersAreNo || bothHaveNotChanged || selfENotChangedAndEmploymentNo || empNotChangedAndSENo
    result

  #Definitions end, starts execution
  changeText()

  $("#" + employment_yes).on "click", changeText
  $("#" + employment_no).on "click", changeText
  $("#" + selfEmployment_yes).on "click", changeText
  $("#" + selfEmployment_no).on "click", changeText


