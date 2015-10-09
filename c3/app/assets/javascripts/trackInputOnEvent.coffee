valueEntered = (selector) -> if ($("##{selector}").val())!="" then "yes" else "no"
window.trackInputOnEventInit = (label, trackChangesTo, whenEventTriggered) ->

  if $("#" + trackChangesTo)
    $("." + whenEventTriggered).on "click", ->
      trackEvent(window.location.pathname, label, valueEntered(trackChangesTo))


