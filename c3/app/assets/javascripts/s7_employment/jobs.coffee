$ ->
    $("#jobs").hide() if $("ul.trip-data").children().length is 0

    $(".breaks-prompt").hide()

    $("ul").on "click", "input[id='changeButton']", ->
        li = $(this).closest("li")
        window.location.href = "/employment/job/" + li.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

    $("ul").on "click", "input[id='deleteButton']", ->
        disable()
        enableConfirmation()

        li = $(this).closest("li")
        ul = $(this).closest("ul")

        $("#jobs .breaks-prompt").slideDown {
            start:->
                dynamicMessage()
            ,complete:->
                $("input[id='noDeleteButton']").unbind "click"
                $("input[id='yesDeleteButton']").unbind "click"

                $("input[id='noDeleteButton']").on "click", ->
                    enable()
                    $(".breaks-prompt").slideUp()

                $("input[id='yesDeleteButton']").on "click", ->
                    disableConfirmation()
                    id = li.attr("id")
                    $("#deleteId").val(id)
                    $(this).parents("form").submit()
        }

dynamicMessage = ->
  normalMsg = $(".normalMsg")
  warningMsg = $(".warningMsg")
  if $("ul").find("tr").length is 1
    normalMsg.hide()
    warningMsg.show()
  else
    normalMsg.show()
    warningMsg.hide()

enableConfirmation = ->
    $("#jobs .breaks-prompt input[type='button']").removeAttr("disabled").removeClass("disabled")

enable = ->
    $("li input[type='button']").removeAttr("disabled").removeClass("disabled")
    $("input[type='radio']").removeAttr("disabled", "true").removeClass("disabled")
    $(".form-steps").children().removeAttr("disabled").removeClass("disabled")

disableConfirmation = ->
    $("#jobs .breaks-prompt input[type='button']").attr("disabled", "true").addClass("disabled")

disable = ->
    $("li input[type='button']").attr("disabled", "true").addClass("disabled")
    $("input[type='radio']").attr("disabled", "true").addClass("disabled")
    $(".form-steps").children().attr("disabled", "true").addClass("disabled")

window.initEvents = (beenEmployed) ->
  # we are returning a function here to assign it to 'conditionRequired' and which will be executed in trackSubmit.scala.html.
  return ->
    $("input[name=" + beenEmployed+"]:checked").val() == "no"