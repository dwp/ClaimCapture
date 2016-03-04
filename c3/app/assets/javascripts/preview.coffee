window.trackChange = ->
$(".review-action a.button").click ->
  id = $(this).attr("id")
  trackEvent('check-your-answers','change',id)

$(".review-action a.previewChangeLink").click ->
  id = $(this).attr("id")
  trackEvent('check-your-answers','change',id)

$(".button-print").click ->
  trackEvent('check-your-answers','print')