window.initEvents = (check,input) ->
  selector = "#" + check
  $(selector).change ->
    if $(selector).is(':checked')
      $("#nameOrOrgWrapper").slideDown 500
    else
      $("#nameOrOrgWrapper").slideUp 500, -> $("#"+input).val("")