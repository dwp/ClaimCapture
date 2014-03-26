$ ->

  $("input[listWithOther=true]").each ->
    $(this).closest("ul").parents("li").next().next().hide() if (($(this).val() == "Somewhere_else") && ($(this).is("checked") == false))

  $("input[listWithOther=true]").change ->
    if $(this).val() is "Somewhere_else"
      $(this).closest("ul").parents("li").next().next().slideDown()
    else
      textArea = $(this).closest("ul").parents("li").next().next().find("textarea")
      $(this).closest("ul").parents("li").next().next().slideUp -> textArea.val("")