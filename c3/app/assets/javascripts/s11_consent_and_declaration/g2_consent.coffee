window.initEvents = (informationFromEmployerY, informationFromEmployerN,informationFromPersonY,informationFromPersonN,why,whyWrapper,whyPerson,whyPersonWrapper) ->
  $("#" + informationFromEmployerY).on "click", ->
    $("#"+whyWrapper).slideUp 0, -> $("#"+why).val("")

  $("#" + informationFromEmployerN).on "click", ->
    $("#"+whyWrapper).slideDown 0

  $("#" + informationFromPersonY).on "click", ->
    $("#"+whyPersonWrapper).slideUp 0, -> $("#"+whyPerson).val("")

  $("#" + informationFromPersonN).on "click", ->
    $("#"+whyPersonWrapper).slideDown 0