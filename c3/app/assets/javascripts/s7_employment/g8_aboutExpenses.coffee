window.initEvents = (
        haveExpensesForJobYes, haveExpensesForJobNo, whatExpensesForJob,jobTitle
        payAnyoneToLookAfterChildrenYes,payAnyoneToLookAfterChildrenNo,
        nameLookAfterChildren,howMuchLookAfterChildren, howOftenLookAfterChildren,
        relationToYouLookAfterChildren,relationToPersonLookAfterChildren,
        payAnyoneToLookAfterPersonYes,payAnyoneToLookAfterPersonNo,
        nameLookAfterPerson,howMuchLookAfterPerson, howOftenLookAfterPerson,
        relationToYouLookAfterPerson,relationToPersonLookAfterPerson) ->

  if not $("#" + haveExpensesForJobYes).prop('checked')
    hideHaveExpensesForJobWrap(whatExpensesForJob, jobTitle)

  if not $("#" + payAnyoneToLookAfterChildrenYes).prop('checked')
    hidePayToLookAfterChildrenJobWrap(nameLookAfterChildren, howMuchLookAfterChildren, howOftenLookAfterChildren, relationToYouLookAfterChildren, relationToPersonLookAfterChildren)

  if not $("#" + payAnyoneToLookAfterPersonYes).prop('checked')
    hidePayToLookAfterPersonJobWrap(nameLookAfterPerson, howMuchLookAfterPerson, howOftenLookAfterPerson, relationToYouLookAfterPerson, relationToPersonLookAfterPerson)

  $("#" + haveExpensesForJobYes).on "click", ->
    showHaveExpensesForJobWrap()

  $("#" + haveExpensesForJobNo).on "click", ->
    hideHaveExpensesForJobWrap(whatExpensesForJob, jobTitle)

  $("#" + payAnyoneToLookAfterChildrenYes).on "click", ->
    showPayToLookAfterChildrenJobWrap()

  $("#" + payAnyoneToLookAfterChildrenNo).on "click", ->
    hidePayToLookAfterChildrenJobWrap(nameLookAfterChildren, howMuchLookAfterChildren, howOftenLookAfterChildren, relationToYouLookAfterChildren, relationToPersonLookAfterChildren)

  $("#" + payAnyoneToLookAfterPersonYes).on "click", ->
    showPayToLookAfterPersonJobWrap()

  $("#" + payAnyoneToLookAfterPersonNo).on "click", ->
    hidePayToLookAfterPersonJobWrap(nameLookAfterPerson, howMuchLookAfterPerson, howOftenLookAfterPerson, relationToYouLookAfterPerson, relationToPersonLookAfterPerson)

showHaveExpensesForJobWrap = ->
  $("#haveExpensesForJobWrap").slideDown 0

hideHaveExpensesForJobWrap = (whatExpensesForJob, jobTitle) ->
  $("#haveExpensesForJobWrap").slideUp 0, ->
    $("#" + whatExpensesForJob).val("")
    $("#" + jobTitle).val("")

showPayToLookAfterChildrenJobWrap = ->
  $("#payToLookAfterChildrenJobWrap").slideDown 0

hidePayToLookAfterChildrenJobWrap = (nameLookAfterChildren, howMuchLookAfterChildren, howOftenLookAfterChildren, relationToYouLookAfterChildren, relationToPersonLookAfterChildren) ->
  $("#payToLookAfterChildrenJobWrap").slideUp 0, ->
    $("#" + nameLookAfterChildren).val("")
    $("#" + howMuchLookAfterChildren).val("")
    $("#" + howOftenLookAfterChildren).val("")
    $("#" + relationToYouLookAfterChildren).val("")
    $("#" + relationToPersonLookAfterChildren).val("")

showPayToLookAfterPersonJobWrap = ->
  $("#payToLookAfterPersonJobWrap").slideDown 0

hidePayToLookAfterPersonJobWrap = (nameLookAfterPerson, howMuchLookAfterPerson, howOftenLookAfterPerson, relationToYouLookAfterPerson, relationToPersonLookAfterPerson) ->
  $("#payToLookAfterPersonJobWrap").slideUp 0, ->
    $("#" + nameLookAfterPerson).val("")
    $("#" + howMuchLookAfterPerson).val("")
    $("#" + howOftenLookAfterPerson).val("")
    $("#" + relationToYouLookAfterPerson).val("")
    $("#" + relationToPersonLookAfterPerson).val("")