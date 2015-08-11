$ ->
  $("#trips").hide() if $("ul.trip-data").children().length is 0

  $(".breaks-prompt").hide()

  $("ul").on "click", "input[name='changerow']", ->
    li = $(this).closest("li")
    window.location.href = "/about-you/trip/" + li.attr("id")

  $("#backButton").on "click", (event) ->
    if ($("#backButton").attr("disabled") == "disabled")
      event.preventDefault()

  $("ul").on "click", "input[name='deleterow']", ->
    disable()
    enableConfirmation()

    li = $(this).closest("li")
    ul = $(this).closest("ul")

    $("#trips .breaks-prompt").slideDown ->
      $("input[name='no']").unbind "click"
      $("input[name='yes']").unbind "click"

      $("input[name='no']").on "click", ->
        enable()
        $(".breaks-prompt").slideUp()

      $("input[name='yes']").on "click", ->
        disableConfirmation()

        $.ajax
          type: "DELETE"
          url: "/about-you/trip/" + li.attr("id")

          success: (data) ->
            $("#anyTrips").parent().children().eq(0).text(data.anyTrips)
            enable()

            $("#trips .breaks-prompt").slideUp()

            if li.closest("ul").children().length is 1
              $("#trips").wrapInner("<div />").children().slideUp -> li.remove()
            else
              li.find("dd").wrapInner "<div>"
              li.find("dd div:not(:last)").slideUp()
              li.find("dd div:last").slideUp -> li.remove()

          error: ->
            location.reload(true)

enableConfirmation = ->
  $("#trips .breaks-prompt input[type='button']").removeAttr("disabled").removeClass("disabled")

enable = ->
  $("li input[type='button']").removeAttr("disabled").removeClass("disabled")
  $("input[type='radio']").removeAttr("disabled", "true").removeClass("disabled")
  $(".form-steps").children().removeAttr("disabled").removeClass("disabled")

disableConfirmation = ->
  $("#trips .breaks-prompt input[type='button']").attr("disabled", "true").addClass("disabled")

disable = ->
  $("li input[type='button']").attr("disabled", "true").addClass("disabled")
  $("input[type='radio']").attr("disabled", "true").addClass("disabled")
  $(".form-steps").children().attr("disabled", "true").addClass("disabled")

window.initEvents = (answer_yes, answer_no) ->
  if ($("#" + answer_yes).is ":checked") && $("ul").children().length is 5
    $("#warningMessageWrap").slideDown()
    $("#warningMessageWrap").css('display', "block")

  $("#" + answer_yes).on "click", ->
    if $("ul.trip-data").children().length is 5
      $("#warningMessageWrap").slideDown()
      $("#warningMessageWrap").css('display', "block")

  $("#" + answer_no).on "click", ->
    $("#warningMessageWrap").slideUp()