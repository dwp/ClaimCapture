$ ->
    $("select[selectWithOther=true] option:selected").each ->
        $(this).parent().next().slideUp(0) if ($(this).val().toLowerCase() != "other")

    $("select[selectWithOther=true]").change ->
        if $(this).val().toLowerCase() is "other"
            $(this).next().slideDown(0)
        else
            textArea = $(this).parent().next().find("textarea")
            $(this).next().slideUp {duration:0,complete:-> textArea.val("")}