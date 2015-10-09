valueEntered = (selector) -> if $("##{selector}").val()? then "yes" else "no"
window.trackInputOnEventInit = (path, label, trackChangesTo, whenEventTriggered) ->

  if $("#" + trackChangesTo)
    $("." + whenEventTriggered).on "click", ->
      trackChange(path, label, trackChangesTo);

trackChange = (path, label, trackChangesTo) ->
  trackEvent(path, 'edit', label, valueEntered(trackChangesTo));


