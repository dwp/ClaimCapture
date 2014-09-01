window.initEvents = (check,input) ->
  if not $("#" + check).prop 'checked'
    hideNameOrgWrapper(input)

  $("#" + check).change ->
    if $("#" + check).is(':checked')
      showNameOrgWrapper()
    else
      hideNameOrgWrapper(input)

hideNameOrgWrapper = (input)->
  $("#nameOrOrgWrapper").slideUp 0, -> $("#"+input).val("")

showNameOrgWrapper = ->
  $("#nameOrOrgWrapper").slideDown 0