window.updateNextLabel = (o) ->
  changeText = ->
    button = $("button.button")
    if goToPreview()
      button.text(o.textReturn)
    else
      button.text(o.textNext)

  goToPreview = ->
    bPreviousEmployed = if o.previousEmployed == "yes" then true else false
    bPreviousSelfEmployed = if o.previousSelfEmployed == "yes" then true else false
    beenEmployed = $("#" + o.employment_yes).is ":checked"
    beenSelfEmployed = $("#" + o.selfEmployment_yes).is ":checked"
    employmentHasChanged = beenEmployed != bPreviousEmployed
    selfEmploymentHasChanged = beenSelfEmployed != bPreviousSelfEmployed
    bothHaveNotChanged = !employmentHasChanged && !selfEmploymentHasChanged
    selfENotChangedAndEmploymentNo = bPreviousEmployed && !beenEmployed && !selfEmploymentHasChanged
    empNotChangedAndSENo = bPreviousSelfEmployed && !beenSelfEmployed && !employmentHasChanged
    bothAnswersAreNo = !beenEmployed && !beenSelfEmployed
    doesNotHaveJobs = beenEmployed && o.noJobs
    result = if(doesNotHaveJobs) then false else bothAnswersAreNo || bothHaveNotChanged || selfENotChangedAndEmploymentNo || empNotChangedAndSENo
    if(result == false) then result else hasOtherPaymentsChanged()

  hasOtherPaymentsChanged = ->
    bPreviousStatutorySickPay = if o.previousStatutorySickPay == "true" then true else false
    bPreviousStatutoryPay = if o.previousStatutoryPay == "true" then true else false
    bPreviousFosteringAllowance = if o.previousFosteringAllowance == "true" then true else false
    bPreviousDirectPayment = if o.previousDirectPayment == "true" then true else false
    bPreviousOtherPayments = if o.previousOtherPayments == "true" then true else false
    statutorySickPay = $("#" + o.sickPay).is ":checked"
    statutoryPay = $("#" + o.statutoryPay).is ":checked"
    fosteringAllowance = $("#" + o.fostering).is ":checked"
    directPayment = $("#" + o.directPayment).is ":checked"
    otherPayments = $("#" + o.otherPayments).is ":checked"
    statutorySickPayChanged = !bPreviousStatutorySickPay && statutorySickPay
    statutorySickChanged = !bPreviousStatutoryPay && statutoryPay
    fosteringAllowanceChanged = !bPreviousFosteringAllowance && fosteringAllowance
    directPaymentChanged = !bPreviousDirectPayment && directPayment
    otherPaymentsChanged = !bPreviousOtherPayments && otherPayments
    !(statutorySickPayChanged || statutorySickChanged || fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)

  #Definitions end, starts execution
  changeText()

  $("#" + o.employment_yes).on "click", changeText
  $("#" + o.employment_no).on "click", changeText
  $("#" + o.selfEmployment_yes).on "click", changeText
  $("#" + o.selfEmployment_no).on "click", changeText
  $("#" + o.statutorySickPay).on "click", changeText
  $("#" + o.statutoryPay).on "click", changeText
  $("#" + o.fostering).on "click", changeText
  $("#" + o.directPayment).on "click", changeText
  $("#" + o.otherPayments).on "click", changeText
  $("#" + o.noPayments).on "click", changeText


