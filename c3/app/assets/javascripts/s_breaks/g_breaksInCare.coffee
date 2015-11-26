window.initSummary = (deleteId) ->
    $("#breaks").hide() if $("ul.break-data").children().length is 0

    $(".breaks-prompt").hide()

    $("ul").on "click", "input[name='changerow']", ->
        li = $(this).closest("li")
        window.location.href = "/breaks/break/" + li.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

    if( $("#warningMessageWrap").length >0)
      $("#warningMessageWrap").hide()

    $("ul").on "click", "input[name='deleterow']", ->


        li = $(this).closest("li")
        prompt = li.next()
        prompt.slideDown ->
            prompt.find("#noDelete").on "click", ->
                prompt.slideUp()

            prompt.find("#yesDelete").on "click", ->
                id = li.attr("id")
                $("#"+deleteId).val(id)
                $("#deleteForm").submit()


window.initEvents = (answer_yes, answer_no, testMode) ->
  $("#" + answer_yes).on "click", ->
    if( $("#warningMessageWrap").length >0)
          $("#warningMessageWrap").show()
          if not testMode then trackEvent(window.location.pathname, "Error", $("#warningMessageWrap").text().trim())


  $("#" + answer_no).on "click", ->
    if( $("#warningMessageWrap").length >0)
      $("#warningMessageWrap").hide()


window.updateNextLabel = (answer_yes,answer_no,textNext,textReturn) ->
  button = $("button.button")
  if $("#" + answer_yes).is ":checked"
    button.text(textNext)

  $("#" + answer_yes).on "click", ->
    button.text(textNext)

  $("#" + answer_no).on "click", ->
    button.text(textReturn)
