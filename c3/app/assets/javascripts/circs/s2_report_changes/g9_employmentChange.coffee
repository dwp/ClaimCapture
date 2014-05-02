window.stillCaring = (stillCaringY, stillCaringN, whenStoppedCaring) ->
  $("#" + stillCaringN).on "click", ->
    $("#stillCaringWrap").slideDown 500
    $("#stillCaringWrap").css('display', "block")

  $("#" + stillCaringY).on "click", ->
    $("#stillCaringWrap").slideUp 500, ->
      $("#" + whenStoppedCaring).val("")

window.hasWorkStartedYet = (hasWorkStartedYetY, hasWorkStartedYetN, dateWhenStarted) ->
  $("#" + hasWorkStartedYetY).on "click", ->
    $("#hasWorkStartedYetWrap").slideDown 500
    $("#hasWorkStartedYetWrap").css('display', "block")

  $("#" + hasWorkStartedYetN).on "click", ->
    $("#hasWorkStartedYetWrap").slideUp 500, ->
      $("#" + dateWhenStarted).val("")

window.hasWorkFinishedYet = (hasWorkFinishedYetY, hasWorkFinishedYetN, dateWhenFinished) ->
  $("#" + hasWorkFinishedYetY).on "click", ->
    $("#hasWorkFinishedYetWrap").slideDown 500
    $("#hasWorkFinishedYetWrap").css('display', "block")

  $("#" + hasWorkFinishedYetN).on "click", ->
    $("#hasWorkFinishedYetWrap").slideUp 500, ->
      $("#" + dateWhenFinished).val("")
