window.initEvents =(finishedThisJobY, finishedThisJobYN, lastWorkDateDay, lastWorkDateMonth, lastWorkDateYear, p45LeavingDateDay, p45LeavingDateMonth, p45LeavingDateYear, hoursPerWeek) ->

  if not $("#" + finishedThisJobY).prop('checked')
    hideLastWorkData(lastWorkDateDay, lastWorkDateMonth, lastWorkDateYear, p45LeavingDateDay, p45LeavingDateMonth, p45LeavingDateYear, hoursPerWeek)

  $("#" + finishedThisJobY).on "click", ->
    showLastWorkData()

  $("#" + finishedThisJobYN).on "click", ->
    hideLastWorkData(lastWorkDateDay, lastWorkDateMonth, lastWorkDateYear, p45LeavingDateDay, p45LeavingDateMonth, p45LeavingDateYear, hoursPerWeek)


showLastWorkData = ->
  $("#lastWorkData").slideDown 0

hideLastWorkData = (lastWorkDateDay, lastWorkDateMonth, lastWorkDateYear, p45LeavingDateDay, p45LeavingDateMonth, p45LeavingDateYear, hoursPerWeek) ->
  $("#" + lastWorkDateDay).val("")
  $("#" + lastWorkDateMonth).val("")
  $("#" + lastWorkDateYear).val("")
  $("#" + p45LeavingDateDay).val("")
  $("#" + p45LeavingDateMonth).val("")
  $("#" + p45LeavingDateYear).val("")
  $("#" + hoursPerWeek).val("")
  $("#lastWorkData").slideUp 0
