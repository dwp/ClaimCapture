window.initEvents = (likeToPay) ->
  return -> $("input[name=" + likeToPay+"]:checked").val() != "bankBuildingAccount"
