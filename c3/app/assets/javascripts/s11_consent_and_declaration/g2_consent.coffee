window.initEvents = (informationFromEmployerY, informationFromEmployerN,informationFromPersonY,informationFromPersonN,why,whyWrapper,whyPerson,whyPersonWrapper) ->

  if not $("#" + informationFromEmployerN).prop 'checked'
    hideWhyWrapper(whyWrapper)

  if not $("#" + informationFromPersonN).prop 'checked'
    hideWhyPersonWrapper(whyPersonWrapper)

  $("#" + informationFromEmployerY).on "click", ->
    hideWhyWrapper(whyWrapper,why)

  $("#" + informationFromEmployerN).on "click", ->
    showWhyWrapper(whyWrapper)

  $("#" + informationFromPersonY).on "click", ->
    hideWhyPersonWrapper(whyPersonWrapper,whyPerson)

  $("#" + informationFromPersonN).on "click", ->
    showWhyPersonWrapper(whyPersonWrapper)


showWhyWrapper = (whyWrapper) ->
  $("#"+whyWrapper).slideDown 0

hideWhyWrapper = (whyWrapper,why)->
  $("#"+whyWrapper).slideUp 0, -> $("#"+why).val("")

hideWhyPersonWrapper = (whyPersonWrapper,whyPerson) ->
  $("#"+whyPersonWrapper).slideUp 0, -> $("#"+whyPerson).val("")

showWhyPersonWrapper = (whyPersonWrapper) ->
  $("#"+whyPersonWrapper).slideDown 0