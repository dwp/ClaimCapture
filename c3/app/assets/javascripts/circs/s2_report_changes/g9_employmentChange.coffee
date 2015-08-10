window.stillCaring = (stillCaringY, stillCaringN, whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear) ->
  if not $("#" + stillCaringN).prop("checked")
    hideStillCaringWrap(whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear)

  $("#" + stillCaringN).on "click", ->
    showStillCaringWrap()

  $("#" + stillCaringY).on "click", ->
    hideStillCaringWrap(whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear)

window.hasWorkStartedYet = (hasWorkStartedYetY, hasWorkStartedYetN, dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear, hasWorkFinishedYetYes, hasWorkFinishedYetNo, dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  if not $("#" + hasWorkStartedYetY).prop("checked")
    hideHasWorkStartedYetWrap(dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, hasWorkFinishedYetYes, hasWorkFinishedYetNo)
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

  if not $("#" + hasWorkStartedYetN).prop("checked")
    hideWorkNotStartedYetWrap(dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear)

  $("#" + hasWorkStartedYetY).on "click", ->
    showHasWorkStartedYetWrap()
    hideWorkNotStartedYetWrap(dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear)

  $("#" + hasWorkStartedYetN).on "click", ->
    showWorkNotStartedYetWrap()
    hideHasWorkStartedYetWrap(dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, hasWorkFinishedYetYes, hasWorkFinishedYetNo)
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

window.hasWorkFinishedYet = (hasWorkFinishedYetY, hasWorkFinishedYetN, dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  if not $("#" + hasWorkFinishedYetY).prop("checked")
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

  $("#" + hasWorkFinishedYetY).on "click", ->
    showHasWorkFinishedYetWrap()

  $("#" + hasWorkFinishedYetN).on "click", ->
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

window.typeOfWork = (typeOfWorkEmployed, typeOfWorkSelfEmployed, employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber, employerPayrollNumber, selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges) ->
  if not $("#" + typeOfWorkEmployed).prop("checked")
    hideTypeOfWorkEmployedWrap(employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber, employerPayrollNumber)

  if not $("#" + typeOfWorkSelfEmployed).prop("checked")
    hideTypeOfWorkSelfEmployedWrap(selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges)

  $("#" + typeOfWorkEmployed).on "click", ->
    showTypeOfWorkEmployedWrap()
    hideTypeOfWorkSelfEmployedWrap(selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges)

  $("#" + typeOfWorkSelfEmployed).on "click", ->
    hideTypeOfWorkEmployedWrap(employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber, employerPayrollNumber)
    showTypeOfWorkSelfEmployedWrap()

showStillCaringWrap = () ->
  $("#stillCaringWrap").slideDown 0

hideStillCaringWrap = (whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear) ->
  $("#stillCaringWrap").slideUp 0, ->
    $("#" + whenStoppedCaringDay).val("")
    $("#" + whenStoppedCaringMonth).val("")
    $("#" + whenStoppedCaringYear).val("")

showHasWorkStartedYetWrap = () ->
  $("#hasWorkStartedYetWrap").slideDown 0

hideWorkNotStartedYetWrap = (dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear) ->
  $("#workNotStartedYetWrap").slideUp 0, ->
    $("#" + dateWhenWillItStartDay).val("")
    $("#" + dateWhenWillItStartMonth).val("")
    $("#" + dateWhenWillItStartYear).val("")

showWorkNotStartedYetWrap = () ->
  $("#workNotStartedYetWrap").slideDown 0

hideHasWorkStartedYetWrap = (dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, hasWorkFinishedYetYes, hasWorkFinishedYetNo) ->
  $("#hasWorkStartedYetWrap").slideUp 0, ->
    $("#" + dateWhenStartedDay).val("")
    $("#" + dateWhenStartedMonth).val("")
    $("#" + dateWhenStartedYear).val("")
    $("#" + hasWorkFinishedYetYes).prop('checked', false)
    $("#" + hasWorkFinishedYetNo).prop('checked', false)

hideHasWorkFinishedYetWrap = (dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  $("#hasWorkFinishedYetWrap").slideUp 0, ->
    $("#" + dateWhenFinishedDay).val("")
    $("#" + dateWhenFinishedMonth).val("")
    $("#" + dateWhenFinishedYear).val("")

showHasWorkFinishedYetWrap = () ->
  $("#hasWorkFinishedYetWrap").slideDown 0

hideTypeOfWorkEmployedWrap = (employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber, employerPayrollNumber) ->
  $("#typeOfWorkEmployedWrap").slideUp 0, ->
    $("#" + employerNameAndAddress1).val("")
    $("#" + employerNameAndAddress2).val("")
    $("#" + employerNameAndAddress3).val("")
    $("#" + employerPostcode).val("")
    $("#" + employerContactNumber).val("")
    $("#" + employerPayrollNumber).val("")

showTypeOfWorkEmployedWrap = () ->
  $("#typeOfWorkEmployedWrap").slideDown 0

hideTypeOfWorkSelfEmployedWrap = (selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges) ->
  $("#typeOfWorkSelfEmployedWrap").slideUp 0, ->
    $("#" + selfEmployedTypeOfWork).val("")
    $("#" + selfEmployedTotalIncomeYes).prop('checked', false)
    $("#" + selfEmployedTotalIncomeNo).prop('checked', false)
    $("#" + selfEmployedTotalIncomeDontKnow).prop('checked', false)
    $("#" + selfEmployedMoreAboutChanges).val("")

showTypeOfWorkSelfEmployedWrap = () ->
  $("#typeOfWorkSelfEmployedWrap").slideDown 0