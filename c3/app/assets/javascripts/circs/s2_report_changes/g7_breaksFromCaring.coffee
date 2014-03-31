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

window.wherePersonBreaksInCareEvents = (wrapperId, inHospital, inRespiteCare, onHoliday, atHome, somewhereElse, text) ->
  $("#" + inHospital).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + inRespiteCare).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + onHoliday).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + atHome).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + somewhereElse).on "click", ->
    $("#" + wrapperId).slideDown 500
    $("#" + wrapperId).css('display', "block")


window.whereYouBreaksInCareEvents = (wrapperId, inHospital, onHoliday, atHome, somewhereElse, text) ->
  $("#" + inHospital).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + onHoliday).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + atHome).on "click", ->
    $("#" + wrapperId).slideUp 500, ->
      $("#" + text).val("")

  $("#" + somewhereElse).on "click", ->
    $("#" + wrapperId).slideDown 500
    $("#" + wrapperId).css('display', "block")

