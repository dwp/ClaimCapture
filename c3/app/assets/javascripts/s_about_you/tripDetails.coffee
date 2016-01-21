isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents = (answer_yes, answer_no, tripDetails) ->
  $("#"+tripDetails).trigger("blur")

  if not isChecked(answer_yes)
    hideTripDetailsWrapper(tripDetails)

  S(answer_yes).on "click", ->
    showTripDetailsWrapper(tripDetails)

  S(answer_no).on "click", ->
    hideTripDetailsWrapper(tripDetails)

hideTripDetailsWrapper = (tripDetails) ->
  val(tripDetails,"")
  S("tripDetailsWrapper").slideUp(0).attr 'aria-hidden', 'true'

showTripDetailsWrapper = (tripDetails) ->
  $("#"+tripDetails).trigger("blur")
  S("tripDetailsWrapper").slideDown(0).attr 'aria-hidden', 'false'