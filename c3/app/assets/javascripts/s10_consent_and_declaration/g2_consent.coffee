window.initEvents = (informationFromEmployerY, informationFromEmployerN,informationFromPersonY,informationFromPersonN,why,whyWrapper,whyPerson,whyPersonWrapper) ->
  $("#" + informationFromEmployerY).on "click", ->
    $("#"+whyWrapper).slideUp 500, -> $("#"+why).val("")

  $("#" + informationFromEmployerN).on "click", ->
    $("#"+whyWrapper).slideDown 500

  $("#" + informationFromPersonY).on "click", ->
    $("#"+whyPersonWrapper).slideUp 500, -> $("#"+whyPerson).val("")

  $("#" + informationFromPersonN).on "click", ->
    $("#"+whyPersonWrapper).slideDown 500