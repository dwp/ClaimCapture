window.stillCaring = (stillCaringY, stillCaringN, whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear, moreAboutChanges) ->
  $("#" + moreAboutChanges).trigger("blur")
  if not $("#" + stillCaringN).prop("checked")
    hideStillCaringWrap(whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear)

  $("#" + stillCaringN).on "click", ->
    showStillCaringWrap()

  $("#" + stillCaringY).on "click", ->
    hideStillCaringWrap(whenStoppedCaringDay, whenStoppedCaringMonth, whenStoppedCaringYear)

window.hasWorkStartedYet = (hasWorkStartedYetY, hasWorkStartedYetN, dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear, hasWorkFinishedYetYes, hasWorkFinishedYetNo, dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  if not $("#" + hasWorkStartedYetY).prop("checked")
    hideHasWorkStartedYetWrap(dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, hasWorkFinishedYetYes,
      hasWorkFinishedYetNo)
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

  if not $("#" + hasWorkStartedYetN).prop("checked")
    hideWorkNotStartedYetWrap(dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear)

  $("#" + hasWorkStartedYetY).on "click", ->
    showHasWorkStartedYetWrap()
    hideWorkNotStartedYetWrap(dateWhenWillItStartDay, dateWhenWillItStartMonth, dateWhenWillItStartYear)

  $("#" + hasWorkStartedYetN).on "click", ->
    showWorkNotStartedYetWrap()
    hideHasWorkStartedYetWrap(dateWhenStartedDay, dateWhenStartedMonth, dateWhenStartedYear, hasWorkFinishedYetYes,
      hasWorkFinishedYetNo)
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

window.hasWorkFinishedYet = (hasWorkFinishedYetY, hasWorkFinishedYetN, dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  if not $("#" + hasWorkFinishedYetY).prop("checked")
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

  $("#" + hasWorkFinishedYetY).on "click", ->
    showHasWorkFinishedYetWrap()

  $("#" + hasWorkFinishedYetN).on "click", ->
    hideHasWorkFinishedYetWrap(dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear)

window.typeOfWork = (typeOfWorkEmployed, typeOfWorkSelfEmployed,
                     employerName, employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber,
                     selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, paidMoneyYes, paidMoneyNo, selfEmployedMoreAboutChanges,
                     paidDay, paidMonth, paidYear, paidWrapper) ->
  if not $("#" + typeOfWorkEmployed).prop("checked")
    hideTypeOfWorkEmployedWrap(employerName, employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3,
      employerPostcode, employerContactNumber)

  if not $("#" + typeOfWorkSelfEmployed).prop("checked")
    hideTypeOfWorkSelfEmployedWrap(selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo,
      selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges, paidMoneyYes, paidMoneyNo, paidDay, paidMonth, paidYear, paidWrapper)

  $("#" + typeOfWorkEmployed).on "click", ->
    showTypeOfWorkEmployedWrap()
    hideTypeOfWorkSelfEmployedWrap(selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo,
      selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges, paidMoneyYes, paidMoneyNo, paidDay, paidMonth, paidYear, paidWrapper)

  $("#" + typeOfWorkSelfEmployed).on "click", ->
    hideTypeOfWorkEmployedWrap(employerName, employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3,
      employerPostcode, employerContactNumber)
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
    $("#" + hasWorkFinishedYetYes).parent().removeClass("selected")
    $("#" + hasWorkFinishedYetNo).parent().removeClass("selected")

hideHasWorkFinishedYetWrap = (dateWhenFinishedDay, dateWhenFinishedMonth, dateWhenFinishedYear) ->
  $("#hasWorkFinishedYetWrap").slideUp 0, ->
    $("#" + dateWhenFinishedDay).val("")
    $("#" + dateWhenFinishedMonth).val("")
    $("#" + dateWhenFinishedYear).val("")

showHasWorkFinishedYetWrap = () ->
  $("#hasWorkFinishedYetWrap").slideDown 0

hideTypeOfWorkEmployedWrap = (employerName, employerNameAndAddress1, employerNameAndAddress2, employerNameAndAddress3, employerPostcode, employerContactNumber) ->
  $("#typeOfWorkEmployedWrap").slideUp 0, ->
    $("#" + employerName).val("")
    $("#" + employerNameAndAddress1).val("")
    $("#" + employerNameAndAddress2).val("")
    $("#" + employerNameAndAddress3).val("")
    $("#" + employerPostcode).val("")
    $("#" + employerContactNumber).val("")

showTypeOfWorkEmployedWrap = () ->
  $("#typeOfWorkEmployedWrap").slideDown 0

hideTypeOfWorkSelfEmployedWrap = (selfEmployedTypeOfWork, selfEmployedTotalIncomeYes, selfEmployedTotalIncomeNo, selfEmployedTotalIncomeDontKnow, selfEmployedMoreAboutChanges, paidMoneyYes, paidMoneyNo, paidDay, paidMonth, paidYear, paidWrapper) ->
  $("#" + paidMoneyYes).prop('checked', false)
  $("#" + paidMoneyNo).prop('checked', false)
  hidePaidMoneyYetWrap(paidDay, paidMonth, paidYear, paidWrapper)
  $("#typeOfWorkSelfEmployedWrap").slideUp 0, ->
    $("#" + selfEmployedTypeOfWork).val("")
    $("#" + selfEmployedTotalIncomeYes).prop('checked', false)
    $("#" + selfEmployedTotalIncomeNo).prop('checked', false)
    $("#" + selfEmployedTotalIncomeDontKnow).prop('checked', false)
    $("#" + selfEmployedMoreAboutChanges).val("")
    $("#" + selfEmployedMoreAboutChanges).trigger("blur")


showTypeOfWorkSelfEmployedWrap = () ->
  $("#typeOfWorkSelfEmployedWrap").slideDown 0


window.paidMoneyYet = (Y, N, day, month, year, wrapper) ->
  if not $("#" + Y).prop("checked")
    hidePaidMoneyYetWrap(day, month, year, wrapper)

  $("#" + Y).on "click", ->
    showPaidMoneyYetWrap(wrapper)

  $("#" + N).on "click", ->
    hidePaidMoneyYetWrap(day, month, year, wrapper)

showPaidMoneyYetWrap = (wrapper) ->
  $("#" + wrapper).slideDown 0

hidePaidMoneyYetWrap = (day, month, year, wrapper) ->
  $("#" + wrapper).slideUp 0, ->
    $("#" + day).val("")
    $("#" + month).val("")
    $("#" + year).val("")
