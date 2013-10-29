$ ->
    $("select[selectWithOther=true] option:selected").each ->
        $(this).closest("li").next().hide() if ($(this).val().toLowerCase() != "other")

    $("select[selectWithOther=true]").change ->
        if $(this).val().toLowerCase() is "other"
            $(this).parent().next().slideDown()
        else
            textArea = $(this).parent().next().find("textarea")
            $(this).parent().next().slideUp -> textArea.val("")