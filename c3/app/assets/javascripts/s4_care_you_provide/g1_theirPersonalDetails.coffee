S = (selector) -> $("##{selector}")

careYouProvideWrap = "careYouProvideWrap"

window.showOrHideCareYouProvideDetails = (o) ->
  if (o.showOrHideDetails)
    S(careYouProvideWrap).slideUp 0

