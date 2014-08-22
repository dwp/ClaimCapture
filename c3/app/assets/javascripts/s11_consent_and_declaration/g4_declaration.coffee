window.initEvents = (check,input) ->
  selector = "#" + check
  $(selector).change ->
    if $(selector).is(':checked')
      $("#nameOrOrgWrapper").slideDown 0
    else
      $("#nameOrOrgWrapper").slideUp 0, -> $("#"+input).val("")