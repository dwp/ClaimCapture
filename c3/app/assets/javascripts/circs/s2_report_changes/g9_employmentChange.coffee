window.stillCaring = (stillCaringY, stillCaringN, whenStoppedCaring) ->
  $("#" + stillCaringN).on "click", ->
    $("#stillCaringWrap").slideDown 500
    $("#stillCaringWrap").css('display', "block")

  $("#" + stillCaringY).on "click", ->
    $("#stillCaringWrap").slideUp 500, ->
      $("#" + whenStoppedCaring + "_day").val("")
      $("#" + whenStoppedCaring + "_month").val("")
      $("#" + whenStoppedCaring + "_year").val("")

window.hasWorkStartedYet = (hasWorkStartedYetY, hasWorkStartedYetN, dateWhenStarted, dateWhenWillItStart, hasWorkFinishedYet, dateWhenFinished) ->
  $("#" + hasWorkStartedYetY).on "click", ->
    $("#hasWorkStartedYetWrap").slideDown 500
    $("#hasWorkStartedYetWrap").css('display', "block")
    $("#workNotStartedYetWrap").slideUp 500, ->
      $("#" + dateWhenWillItStart + "_day").val("")
      $("#" + dateWhenWillItStart + "_month").val("")
      $("#" + dateWhenWillItStart + "_year").val("")

  $("#" + hasWorkStartedYetN).on "click", ->
    $("#workNotStartedYetWrap").slideDown 500
    $("#workNotStartedYetWrap").css('display', "block")
    $("#hasWorkStartedYetWrap").slideUp 500, ->
      $("#" + dateWhenStarted + "_day").val("")
      $("#" + dateWhenStarted + "_month").val("")
      $("#" + dateWhenStarted + "_year").val("")
      $("#" + hasWorkFinishedYet + "_yes").prop('checked', false)
      $("#" + hasWorkFinishedYet + "_no").prop('checked', false)
    $("#hasWorkFinishedYetWrap").slideUp 500, ->
      $("#" + dateWhenFinished + "_day").val("")
      $("#" + dateWhenFinished + "_month").val("")
      $("#" + dateWhenFinished + "_year").val("")

window.hasWorkFinishedYet = (hasWorkFinishedYetY, hasWorkFinishedYetN, dateWhenFinished) ->
  $("#" + hasWorkFinishedYetY).on "click", ->
    $("#hasWorkFinishedYetWrap").slideDown 500
    $("#hasWorkFinishedYetWrap").css('display', "block")

  $("#" + hasWorkFinishedYetN).on "click", ->
    $("#hasWorkFinishedYetWrap").slideUp 500, ->
      $("#" + dateWhenFinished + "_day").val("")
      $("#" + dateWhenFinished + "_month").val("")
      $("#" + dateWhenFinished + "_year").val("")

window.typeOfWork = (typeOfWorkEmployed, typeOfWorkSelfEmployed, employerNameAndAddress, employerPostcode, employerContactNumber, employerPayrollNumber, selfEmployedTypeOfWork, selfEmployedTotalIncome) ->
  $("#" + typeOfWorkEmployed).on "click", ->
    $("#typeOfWorkEmployedWrap").slideDown 500
    $("#typeOfWorkEmployedWrap").css('display', "block")
    $("#typeOfWorkSelfEmployedWrap").slideUp 500, ->
      $("#" + selfEmployedTypeOfWork).val("")
      $("#" + selfEmployedTotalIncome + "_yes").prop('checked', false)
      $("#" + selfEmployedTotalIncome + "_no").prop('checked', false)
      $("#" + selfEmployedTotalIncome + "_dontknow").prop('checked', false)

  $("#" + typeOfWorkSelfEmployed).on "click", ->
    $("#typeOfWorkEmployedWrap").slideUp 500, ->
      $("#" + employerNameAndAddress + "_lineOne").val("")
      $("#" + employerNameAndAddress + "_lineTwo").val("")
      $("#" + employerNameAndAddress + "_lineThree").val("")
      $("#" + employerPostcode).val("")
      $("#" + employerContactNumber).val("")
      $("#" + employerPayrollNumber).val("")
    $("#typeOfWorkSelfEmployedWrap").slideDown 500
    $("#typeOfWorkSelfEmployedWrap").css('display', "block")
