$ ->

  $("input[class='listWithOther otherValue']").each ->
    $(this).closest("ul").next().hide() if not ($(this).prop("checked"))

  $("input[class='listWithOther otherValue']").change ->
      $(this).closest("ul").next().slideDown()


  $("input[class='listWithOther']").change ->
    textArea = $(this).closest("ul").next().find("textarea")
    $(this).closest("ul").next().slideUp -> textArea.val("")