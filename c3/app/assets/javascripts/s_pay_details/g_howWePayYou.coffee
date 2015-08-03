window.initEvents = (likeToPay) ->
  # we are returning a function here to assign it to 'conditionRequired' and which will be executed in trackSubmit.scala.html.
  return -> $("input[name=" + likeToPay+"]:checked").val() != "bankBuildingAccount"
