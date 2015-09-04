window.initSummary = (deleteId) ->
    $("#breaks").hide() if $("ul.break-data").children().length is 0

    $(".breaks-prompt").hide()

    $("ul").on "click", "input[name='changerow']", ->
        li = $(this).closest("li")
        window.location.href = "/breaks/break/" + li.attr("id")

    $("#backButton").on "click", (event) ->
        if ($("#backButton").attr("disabled") == "disabled")
            event.preventDefault()

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
