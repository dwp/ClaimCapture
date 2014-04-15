$ ->

  $("input[listWithOther=true]").each ->
    $(this).closest("ul").next().hide() if (($(this).val() == $(this).attr("othervalue")) && ($(this).attr("checked") == undefined))

  $("input[listWithOther=true]").change ->
    if $(this).val() is $(this).attr("othervalue")
      $(this).closest("ul").next().slideDown()
    else
      textArea = $(this).closest("ul").next().find("textarea")
      $(this).closest("ul").next().slideUp -> textArea.val("")