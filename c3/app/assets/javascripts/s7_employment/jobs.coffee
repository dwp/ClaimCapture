$ ->
    $("#jobs").hide() if $("tbody").children().length is 0

    $(".breaks-prompt").hide()

    $("tbody").on "click", "input[value='Change']", ->
        tr = $(this).closest("tr")
        window.location.href = "/employment/job/" + tr.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

    $("tbody").on "click", "input[value='Delete']", ->
        disable()
        enableConfirmation()

        tr = $(this).closest("tr")
        tbody = $(this).closest("tbody")

        $("#jobs .breaks-prompt").slideDown {
            start:->
                dynamicMessage()
            ,complete:->
                $("input[value='No']").unbind "click"
                $("input[value='Yes']").unbind "click"

                $("input[value='No']").on "click", ->
                    enable()
                    $(".breaks-prompt").slideUp()

                $("input[value='Yes']").on "click", ->
                    disableConfirmation()

                    $.ajax
                        type: "DELETE"
                        url: "/employment/job/" + tr.attr("id")

                        success: (data) ->
                            $("label[for='answer']").text(data.answer)
                            enable()

                            $("#jobs .breaks-prompt").slideUp()

                            if tr.closest("tbody").children().length is 1
                                $("#jobs").wrapInner("<div />").children().slideUp -> tr.remove()
                            else
                                tr.find("td").wrapInner "<div>"
                                tr.find("td div:not(:last)").slideUp()
                                tr.find("td div:last").slideUp -> tr.remove()

                        error: ->
                            location.reload(true)
        }

dynamicMessage = ->
  normalMsg = $(".normalMsg")
  warningMsg = $(".warningMsg")
  if $("tbody").find("tr").length is 1
    normalMsg.hide()
    warningMsg.show()
  else
    normalMsg.show()
    warningMsg.hide()

enableConfirmation = ->
    $("#jobs .breaks-prompt input[type='button']").removeAttr("disabled").removeClass("disabled")

enable = ->
    $("tr input[type='button']").removeAttr("disabled").removeClass("disabled")
    $("input[type='radio']").removeAttr("disabled", "true").removeClass("disabled")
    $(".form-steps").children().removeAttr("disabled").removeClass("disabled")

disableConfirmation = ->
    $("#jobs .breaks-prompt input[type='button']").attr("disabled", "true").addClass("disabled")

disable = ->
    $("tr input[type='button']").attr("disabled", "true").addClass("disabled")
    $("input[type='radio']").attr("disabled", "true").addClass("disabled")
    $(".form-steps").children().attr("disabled", "true").addClass("disabled")