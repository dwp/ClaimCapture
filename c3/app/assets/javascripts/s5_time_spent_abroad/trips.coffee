$ ->
    $("#trips").hide() if $("tbody").children().length is 0

    $(".breaks-prompt").hide()

    $("tbody").on "click", "input[value='Change']", ->
        tr = $(this).closest("tr")
        window.location.href = "/time-spent-abroad/trip/" + tr.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

    $("tbody").on "click", "input[value='Delete']", ->
        disable()

        tr = $(this).closest("tr")
        tbody = $(this).closest("tbody")

        $(".breaks-prompt").slideDown ->
            $("input[value='No']").on "click", ->
                enable()

                $(".breaks-prompt").slideUp()

            $("input[value='Yes']").on "click", ->
                $.ajax
                    type: "DELETE"
                    url: "/time-spent-abroad/trip/" + tr.attr("id")

                    success: (data) ->
                        $("#anyTrips").parent().children().eq(0).text(data.anyTrips)
                        enable()

                        $(".breaks-prompt").slideUp()

                        element = undefined

                        if tbody.children().length is 1
                            element = $("#trips")
                        else
                            element = tr.children("td")

                        element.animate(
                            padding: 0
                            margin: 0
                        ).wrapInner("<div />").children().slideUp -> tr.remove()

                    error: ->
                        enable()

                        $(".breaks-prompt").slideUp()
                        alert "Failed to delete trip - Contact Support"

enable = ->
    $("tr input[type='button']").removeAttr("disabled").removeClass("disabled")
    $("input[type='radio']").removeAttr("disabled", "true").removeClass("disabled")
    $(".form-steps").children().removeAttr("disabled").removeClass("disabled")

disable = ->
    $("tr input[type='button']").attr("disabled", "true").addClass("disabled")
    $("input[type='radio']").attr("disabled", "true").addClass("disabled")
    $(".form-steps").children().attr("disabled", "true").addClass("disabled")