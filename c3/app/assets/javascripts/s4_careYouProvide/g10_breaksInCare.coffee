$ ->
    $("#breaks").hide() if $("tbody").children().length is 0

    $(".breaks-prompt").hide()

    $("tbody").on "click", "input[value='Edit']", ->
        tr = $(this).closest("tr")
        window.location.href = "/careYouProvide/breaks/" + tr.attr("id")

    $("tbody").on "click", "input[value='Delete']", ->
        $("tr input[type='button']").attr("disabled", "true")
        $("tr input[type='button']").addClass("disabled")
        $(".form-steps").hide()

        tr = $(this).closest("tr")
        tbody = $(this).closest("tbody")

        $(".breaks-prompt").slideDown ->
            $("input[value='No']").on "click", ->
                $("tr input[type='button']").removeAttr("disabled")
                $("tr input[type='button']").removeClass("disabled")
                $(".form-steps").slideDown()
                $(".breaks-prompt").slideUp()

            $("input[value='Yes']").on "click", ->
                $.ajax
                    type: "DELETE"
                    url: "/careYouProvide/breaksInCare/" + tr.attr("id")

                    success: (data) ->
                        $("label[for='answer']").text(data.answer)
                        $("tr input[type='button']").removeAttr("disabled")
                        $("tr input[type='button']").removeClass("disabled")
                        $(".form-steps").slideDown()
                        $(".breaks-prompt").slideUp()

                        element = undefined

                        if tbody.children().length is 1
                            element = $("#breaks")
                        else
                            element = tr.children("td")

                        element.animate(
                            padding: 0
                            margin: 0
                        ).wrapInner("<div />").children().slideUp -> tr.remove()

                    error: ->
                        $("tr input[type='button']").removeAttr("disabled")
                        $("tr input[type='button']").removeClass("disabled")
                        $(".form-steps").slideDown()
                        $(".breaks-prompt").slideUp()
                        alert "Failed to delete break - Contact Support"