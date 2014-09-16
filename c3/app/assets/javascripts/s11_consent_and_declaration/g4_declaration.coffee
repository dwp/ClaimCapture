
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

  if not S(o.informationFromEmployerN).prop 'checked'
    hideWhyWrapper(o)

  if not S(o.informationFromPersonN).prop 'checked'
    hideWhyPersonWrapper(o)

  S(o.informationFromEmployerY).on "click", ->
    hideWhyWrapper(o)

  S(o.informationFromEmployerN).on "click", ->
    showWhyWrapper(o)

  S(o.informationFromPersonY).on "click", ->
    hideWhyPersonWrapper(o)

  S(o.informationFromPersonN).on "click", ->
    showWhyPersonWrapper(o)

hideNameOrgWrapper = (o) ->
  S("nameOrOrgWrapper").slideUp 0, -> val(o.input, "")

showNameOrgWrapper = ->
  S("nameOrOrgWrapper").slideDown 0

showWhyWrapper = (o) ->
  S(o.whyWrapper).slideDown 0

hideWhyWrapper = (o) ->
  S(o.whyWrapper).slideUp 0, -> val(o.why, "")

hideWhyPersonWrapper =  (o) ->
  S(o.whyPersonWrapper).slideUp 0, -> val(o.whyPerson, "")

showWhyPersonWrapper = (o) ->
  S(o.whyPersonWrapper).slideDown 0