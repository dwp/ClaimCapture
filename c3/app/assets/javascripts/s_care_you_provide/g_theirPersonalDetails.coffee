S = (selector) -> $("##{selector}")

showElement = (elem) ->
  $("#" + elem).slideDown(0).attr 'aria-hidden', 'false'

hideElement = (elem) ->
  $("#" + elem).slideUp(0).attr 'aria-hidden', 'true'

showAddress = () ->
  showElement "addressWrapper"

hideAddress = () ->
  hideElement "addressWrapper"
  $("#addressWrapper input").val("")

careYouProvideWrap = "careYouProvideWrap"

window.showOrHideCareYouProvideDetails = (o) ->
  if (o.showOrHideDetails)
    S(careYouProvideWrap).slideUp(0).attr 'aria-hidden', 'true'


window.initAddress = (sameAddress_yes, sameAddress_no ) ->

  if($("#" + sameAddress_no).prop('checked'))
    showAddress()
  else
    hideAddress()

  $("#" + sameAddress_yes).on "click", ->
    hideAddress()

  $("#" + sameAddress_no).on "click", ->
    showAddress()

