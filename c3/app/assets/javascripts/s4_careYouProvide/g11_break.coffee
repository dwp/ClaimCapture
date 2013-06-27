$ ->
  $("select[selectWithOther=true]").each ->
    if $(this).val() is "Other" and $(this).selected()
      $(this).parent().next().hide()

  $("select[selectWithOther=true]").change ->
    if $(this).val() is "Other"
      $(this).parent().next().slideDown()
    else
      textArea = $(this).parent().next().find("textarea")
      $(this).parent().next().slideUp ->
        textArea.val ""