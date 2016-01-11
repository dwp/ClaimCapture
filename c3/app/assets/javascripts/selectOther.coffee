$ ->
    $("select[class=selectWithOther] option:selected").each ->
      otherTextArea = $(this).parent().next()

      if $(this).val().toLowerCase() == "other"
        otherTextArea.slideDown(0)
      else
        otherTextArea.slideUp(0)

    $("select[class=selectWithOther]").change ->
        if $(this).val().toLowerCase() is "other"
            $(this).parent().find("textarea").trigger("blur")
            $(this).next().slideDown(0)
        else
            textArea = $(this).parent().find("textarea")
            $(this).next().slideUp {duration:0,complete:-> textArea.val("")}