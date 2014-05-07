window.stillCaring = (stillCaringY, stillCaringN, whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear) ->
  $("#" + stillCaringN).on "click", ->
    $("#stillCaringWrap").slideDown 500
    $("#stillCaringWrap").css('display', "block")

  $("#" + stillCaringY).on "click", ->
    $("#stillCaringWrap").slideUp 500, ->
      $("#" + whenStoppedCaringDay).val("")
      $("#" + whenStoppedCaringMonth).val("")
      $("#" + whenStoppedCaringYear).val("")

window.hasWorkStartedYet = (hasWorkStartedYetY, hasWorkStartedYetN, dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear, hasWorkFinishedYetYes, hasWorkFinishedYetNo, dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  $("#" + hasWorkStartedYetY).on "click", ->
    $("#hasWorkStartedYetWrap").slideDown 500
    $("#hasWorkStartedYetWrap").css('display', "block")
    $("#workNotStartedYetWrap").slideUp 500, ->
      $("#" + dateWhenWillItStartDay).val("")
      $("#" + dateWhenWillItStartMonth).val("")
      $("#" + dateWhenWillItStartYear).val("")

  $("#" + hasWorkStartedYetN).on "click", ->
    $("#workNotStartedYetWrap").slideDown 500
    $("#workNotStartedYetWrap").css('display', "block")
    $("#hasWorkStartedYetWrap").slideUp 500, ->
      $("#" + dateWhenStartedDay).val("")
      $("#" + dateWhenStartedMonth).val("")
      $("#" + dateWhenStartedYear).val("")
      $("#" + hasWorkFinishedYetYes).prop('checked', false)
      $("#" + hasWorkFinishedYetNo).prop('checked', false)
    $("#hasWorkFinishedYetWrap").slideUp 500, ->
      $("#" + dateWhenFinishedDay).val("")
      $("#" + dateWhenFinishedMonth).val("")
      $("#" + dateWhenFinishedYear).val("")

window.hasWorkFinishedYet = (hasWorkFinishedYetY, hasWorkFinishedYetN, dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  $("#" + hasWorkFinishedYetY).on "click", ->
    $("#hasWorkFinishedYetWrap").slideDown 500
    $("#hasWorkFinishedYetWrap").css('display', "block")

  $("#" + hasWorkFinishedYetN).on "click", ->
    $("#hasWorkFinishedYetWrap").slideUp 500, ->
      $("#" + dateWhenFinishedDay).val("")
      $("#" + dateWhenFinishedMonth).val("")
      $("#" + dateWhenFinishedYear).val("")

window.typeOfWork = (typeOfWorkEmployed, typeOfWorkSelfEmployed, employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber, employerPayrollNumber, selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges) ->
  $("#" + typeOfWorkEmployed).on "click", ->
    $("#typeOfWorkEmployedWrap").slideDown 500
    $("#typeOfWorkEmployedWrap").css('display', "block")
    $("#typeOfWorkSelfEmployedWrap").slideUp 500, ->
      $("#" + selfEmployedTypeOfWork).val("")
      $("#" + selfEmployedTotalIncomeYes).prop('checked', false)
      $("#" + selfEmployedTotalIncomeNo).prop('checked', false)
      $("#" + selfEmployedTotalIncomeDontKnow).prop('checked', false)
      $("#" + selfEmployedMoreAboutChanges).val("")

  $("#" + typeOfWorkSelfEmployed).on "click", ->
    $("#typeOfWorkEmployedWrap").slideUp 500, ->
      $("#" + employerNameAndAddress1).val("")
      $("#" + employerNameAndAddress2).val("")
      $("#" + employerNameAndAddress3).val("")
      $("#" + employerPostcode).val("")
      $("#" + employerContactNumber).val("")
      $("#" + employerPayrollNumber).val("")
    $("#typeOfWorkSelfEmployedWrap").slideDown 500
    $("#typeOfWorkSelfEmployedWrap").css('display', "block")
