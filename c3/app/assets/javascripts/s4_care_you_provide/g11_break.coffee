$ ->
    $("select[selectWithOther=true] option:selected").each ->
        $(this).parent().next().hide() if ($(this).val() != "Other")

    $("select[selectWithOther=true]").change ->
        if $(this).val() is "Other"
            $(this).next().slideDown(0)
        else
            textArea = $(this).parent().next().find("textarea")
            $(this).next().slideUp {duration:0,complete:textArea.val("")}