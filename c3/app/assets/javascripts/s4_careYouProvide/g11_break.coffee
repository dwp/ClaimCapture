$ ->
  $("select[selectWithOther=true] option:selected").each ->
    $(this).closest("li").next().hide() if ($(this).text() != "Other")

  $("select[selectWithOther=true]").change ->
    if $(this).val() is "Other"
      $(this).parent().next().slideDown()
    else
      textArea = $(this).parent().next().find("textarea")
      $(this).parent().next().slideUp -> textArea.val("")