$ ->
    $("select[class=selectWithOther] option:selected").each ->
        $(this).parent().next().hide() if ($(this).val() != "Other")

    $("select[class=selectWithOther]").change ->
        if $(this).val() is "Other"
            $(this).next().slideDown(0)
        else
            textArea = $(this).parent().next().find("textarea")
            $(this).next().slideUp {duration:0,complete:textArea.val("")}