$ ->
  $("input[selectWithOther=true]").each ->
    alert($(this).closest("li"))
    $(this).closest("li").next().hide() if ($(this).val() != "Somewhere else")

  $("input[selectWithOther=true]").change ->
    if $(this).val() is "Somewhere else"
      $(this).parent().next().slideDown()
    else
      textArea = $(this).parent().next().find("textarea")
      $(this).parent().next().slideUp -> textArea.val("")