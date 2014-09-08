
isChecked = (selector)  ->  $("##{selector}").prop('checked')
val = (selector,text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents =(o) ->

  if not isChecked(o.finishedThisJobY)
    hideLastWorkData(o)

  if not isChecked(o.startJobBeforeClaimDateN)
    hideJobStartDateWrapper(o)

  S(o.finishedThisJobY).on "click", ->
    showLastWorkData()

  S(o.finishedThisJobN).on "click", ->
    hideLastWorkData(o)

  S(o.startJobBeforeClaimDateN).on "click", ->
    showJobStartDateWrapper()

  S(o.startJobBeforeClaimDateY).on "click", ->
    hideJobStartDateWrapper(o)


showLastWorkData = ->
  S("lastWorkData").slideDown 0

hideLastWorkData = (o) ->
  reset(o.lastWorkData)
  S("lastWorkData").slideUp 0


showJobStartDateWrapper = ->
  S("jobStartDateWrapper").slideDown 0

hideJobStartDateWrapper = (o) ->
  reset(o.jobStartDate)
  S("jobStartDateWrapper").slideUp 0

reset = (resetList) ->
  (val(elem,"") for elem in resetList)

