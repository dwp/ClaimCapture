showElement = (elem) ->
  $("#" + elem).slideDown(0).attr 'aria-hidden', 'false'

hideElement = (elem) ->
  $("#" + elem).slideUp(0).attr 'aria-hidden', 'true'

showBankDetails = () ->
  showElement "bankDetailsWrapper"
  hideElement "noBankDetailsWrapper"

showNoBank = () ->
  hideElement "bankDetailsWrapper"
  showElement "noBankDetailsWrapper"
  $("#bankDetailsWrapper input").val("")

hideAll = () ->
  hideElement "bankDetailsWrapper"
  hideElement "noBankDetailsWrapper"


window.initEvents = (likeToPay, haveBankAccount, noBankAccount) ->
  hideAll()
  if($("#" + haveBankAccount).prop('checked'))
    showBankDetails()

  if($("#" + noBankAccount).prop('checked'))
    showNoBank()

  $("#" + haveBankAccount).on "click", ->
    showBankDetails()

  $("#" + noBankAccount).on "click", ->
    showNoBank()

  # we are returning a function here to assign it to 'conditionRequired' and which will be executed in trackSubmit.scala.html.
  return -> $("input[name=" + likeToPay + "]:checked").val() != "bankBuildingAccount"
