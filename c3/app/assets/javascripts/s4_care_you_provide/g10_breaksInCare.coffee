$ ->
    $("#breaks").hide() if $("tbody").children().length is 0

    $(".breaks-prompt").hide()

    $("tbody").on "click", "input[name='changerow']", ->
        tr = $(this).closest("tr")
        window.location.href = "/care-you-provide/breaks/" + tr.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

    $("tbody").on "click", "input[name='deleterow']", ->
        disable()
        enableConfirmation()

        tr = $(this).closest("tr")

        $("#breaks .breaks-prompt").slideDown ->
            $("input[name='no']").unbind "click"
            $("input[name='yes']").unbind "click"

            $("input[name='no']").on "click", ->
                enable()
                $("#breaks .breaks-prompt").slideUp()

            $("input[name='yes']").on "click", ->
                disableConfirmation()

                $.ajax
                    type: "DELETE"
                    url: "/care-you-provide/breaks-in-care/" + tr.attr("id")

                    success: (data) ->
                        $("label[for='answer']").text(data.answer)
                        enable()

                        $("#breaks .breaks-prompt").slideUp()

                        if tr.closest("tbody").children().length is 1
                            $("#breaks").wrapInner("<div />").children().slideUp -> tr.remove()
                        else
                            tr.find("td").wrapInner "<div>"
                            tr.find("td div:not(:last)").slideUp()
                            tr.find("td div:last").slideUp -> tr.remove()

                    error: ->
                        location.reload(true)

enableConfirmation = ->
    $("#breaks .breaks-prompt input[type='button']").removeAttr("disabled").removeClass("disabled")

enable = ->
    $("tr input[type='button']").removeAttr("disabled").removeClass("disabled")
    $("input[type='radio']").removeAttr("disabled", "true").removeClass("disabled")
    $(".form-steps").children().removeAttr("disabled").removeClass("disabled")

disableConfirmation = ->
    $("#breaks .breaks-prompt input[type='button']").attr("disabled", "true").addClass("disabled")

disable = ->
    $("tr input[type='button']").attr("disabled", "true").addClass("disabled")
    $("input[type='radio']").attr("disabled", "true").addClass("disabled")
    $(".form-steps").children().attr("disabled", "true").addClass("disabled")

window.initEvents = (answer_yes, answer_no) ->
  if ($("#" + answer_yes).is ":checked") && $("tbody").children().length is 10
    $("#warningMessageWrap").slideDown()
    $("#warningMessageWrap").css('display', "block")

  $("#" + answer_yes).on "click", ->
      if $("tbody").children().length is 10
          $("#warningMessageWrap").slideDown()
          $("#warningMessageWrap").css('display', "block")

  $("#" + answer_no).on "click", ->
      $("#warningMessageWrap").slideUp()
