window.initSummary = (deleteId) ->
    $("#breaks").hide() if $("ul.break-data").children().length is 0

    $(".breaks-prompt").hide()

    $("ul").on "click", "input[name='changerow']", ->
        li = $(this).closest("li")
        window.location.href = "/care-you-provide/breaks/" + li.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

    $("ul").on "click", "input[name='deleterow']", ->
        disable()
        enableConfirmation()

        li = $(this).closest("li")

        $("#breaks .breaks-prompt").slideDown ->
            $("input[name='no']").unbind "click"
            $("input[name='yes']").unbind "click"

            $("input[name='no']").on "click", ->
                enable()
                $("#breaks .breaks-prompt").slideUp()

            $("input[name='yes']").on "click", ->
                disableConfirmation()
                id = li.attr("id")
                $("#"+deleteId).val(id)
                $(this).parents("form").submit()

enableConfirmation = ->
    $("#breaks .breaks-prompt input[type='button']").removeAttr("disabled").removeClass("disabled")

enable = ->
    $("li input[type='button']").removeAttr("disabled").removeClass("disabled")
    $("input[type='radio']").removeAttr("disabled", "true").removeClass("disabled")
    $(".form-steps").children().removeAttr("disabled").removeClass("disabled")

disableConfirmation = ->
    $("#breaks .breaks-prompt input[type='button']").attr("disabled", "true").addClass("disabled")

disable = ->
    $("li input[type='button']").attr("disabled", "true").addClass("disabled")
    $("input[type='radio']").attr("disabled", "true").addClass("disabled")
    $(".form-steps").children().attr("disabled", "true").addClass("disabled")

window.initEvents = (answer_yes, answer_no) ->
  if ($("#" + answer_yes).is ":checked") && $("ul").children().length is 10
    $("#warningMessageWrap").slideDown()
    $("#warningMessageWrap").css('display', "block")

  $("#" + answer_yes).on "click", ->
      if $("ul.break-data").children().length is 10
          $("#warningMessageWrap").slideDown()
          $("#warningMessageWrap").css('display', "block")

  $("#" + answer_no).on "click", ->
      $("#warningMessageWrap").slideUp()
