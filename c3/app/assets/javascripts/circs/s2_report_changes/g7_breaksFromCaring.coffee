window.initEvents = (breakEndedY, breakEndedN, expectStartCaringY, expectStartCaringN, expectStartCaringDontKnow) ->
  $("#" + breakEndedY).on "click", ->
    $("#breakEndedDateTime").slideDown 500
    $("#breakEndedDateTime").css('display', "block")
    slideUpExpectStartCaring()

  $("#" + breakEndedN).on "click", ->
    $("#breakEndedDateTime").slideUp 500
    slideDownExpectStartCaring(expectStartCaringY, expectStartCaringN)

  $("#" + expectStartCaringY).on "click", ->
    $("#expectStartCaringDate").slideDown 500
    $("#expectStartCaringDate").css('display', "block")
    $("#permanentBreakDate").slideUp 500

  $("#" + expectStartCaringN).on "click", ->
    $("#permanentBreakDate").slideDown 500
    $("#permanentBreakDate").css('display', "block")
    $("#expectStartCaringDate").slideUp 500

  $("#" + expectStartCaringDontKnow).on "click", ->
    $("#expectStartCaringDate").slideUp 500
    $("#permanentBreakDate").slideUp 500

slideUpExpectStartCaring = ->
  $("#expectStartCaring").slideUp 500
  $("#expectStartCaringDate").slideUp 500
  $("#permanentBreakDate").slideUp 500

slideDownExpectStartCaring = (expectStartCaringY, expectStartCaringN) ->
  $("#expectStartCaring").slideDown 500
  $("#expectStartCaring").css('display', "block")
  $("#" + expectStartCaringY).attr("checked", false)
  $("#" + expectStartCaringN).attr("checked", false)