
S = (selector) -> $("##{selector}")
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()

window.initEvents = (o) ->
  if not S(o.informationFromPersonN).prop 'checked'
    hideWhyPersonWrapper(o)

  S(o.informationFromPersonY).on "click", ->
    hideWhyPersonWrapper(o)

  S(o.informationFromPersonN).on "click", ->
    showWhyPersonWrapper(o)

hideWhyPersonWrapper =  (o) ->
  S(o.whyPersonWrapper).slideUp(0).attr 'aria-hidden', 'true', -> val(o.whyPerson, "")
  $("#whyPersonWrapper textarea").val("")

showWhyPersonWrapper = (o) ->
  S(o.whyPersonWrapper).slideDown(0).attr 'aria-hidden', 'false'