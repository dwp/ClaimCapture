
S = (selector) -> $("##{selector}")
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()

window.initEvents = (o) ->
  if not S(o.check).prop 'checked'
    hideNameOrgWrapper(o)

  S(o.check).change ->
    if S(o.check).is(':checked')
      showNameOrgWrapper()
    else
      hideNameOrgWrapper(o)

  if not S(o.informationFromPersonN).prop 'checked'
    hideWhyPersonWrapper(o)

  S(o.informationFromPersonY).on "click", ->
    hideWhyPersonWrapper(o)

  S(o.informationFromPersonN).on "click", ->
    showWhyPersonWrapper(o)

hideNameOrgWrapper = (o) ->
  S("nameOrOrgWrapper").slideUp(0).attr 'aria-hidden', 'true', -> val(o.input, "")
  $("#nameOrOrgWrapper input").val("")

showNameOrgWrapper = ->
  S("nameOrOrgWrapper").slideDown(0).attr 'aria-hidden', 'false'

hideWhyPersonWrapper =  (o) ->
  S(o.whyPersonWrapper).slideUp(0).attr 'aria-hidden', 'true', -> val(o.whyPerson, "")
  $("#whyPersonWrapper textarea").val("")

showWhyPersonWrapper = (o) ->
  S(o.whyPersonWrapper).slideDown(0).attr 'aria-hidden', 'false'