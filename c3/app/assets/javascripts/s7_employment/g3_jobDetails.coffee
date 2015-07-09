isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents =(o) ->

  updateHoursPerWeekLabel(o)

  if not isChecked(o.finishedThisJobY)
    hideLastWorkData(o)

  if not isChecked(o.startJobBeforeClaimDateN)
    hideJobStartDateWrapper(o)

  S(o.finishedThisJobY).on "click", ->
    showLastWorkData()
    updateHoursPerWeekLabel(o)

  S(o.finishedThisJobN).on "click", ->
    hideLastWorkData(o)
    updateHoursPerWeekLabel(o)

  S(o.startJobBeforeClaimDateN).on "click", ->
    showJobStartDateWrapper()

  S(o.startJobBeforeClaimDateY).on "click", ->
    hideJobStartDateWrapper(o)


showLastWorkData = ->
  S("lastWorkData").slideDown(0).attr 'aria-hidden', 'false'

hideLastWorkData = (o) ->
  reset(o.lastWorkData)
  S("lastWorkData").slideUp(0).attr 'aria-hidden', 'true'


showJobStartDateWrapper = ->
  S("jobStartDateWrapper").slideDown(0).attr 'aria-hidden', 'false'

hideJobStartDateWrapper = (o) ->
  reset(o.jobStartDate)
  S("jobStartDateWrapper").slideUp(0).attr 'aria-hidden', 'true'

reset = (resetList) ->
  (val(elem,"") for elem in resetList)

updateHoursPerWeekLabel = (o) ->
  id = o.hoursPerWeek.id
  finishedThisJob = isChecked(o.finishedThisJobY)
  presentLabel = o.hoursPerWeek.present
  pastLabel = o.hoursPerWeek.past
  labelChildren = $("##{id}").prev().children()
  label = $("##{id}").prev()
  if finishedThisJob
    label.text(pastLabel).append(labelChildren)
  else
    label.text(presentLabel).append(labelChildren)







