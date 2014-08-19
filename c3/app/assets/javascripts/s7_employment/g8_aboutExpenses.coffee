window.initEvents = (
        haveExpensesForJobYes, haveExpensesForJobNo, whatExpensesForJob,
        payAnyoneToLookAfterChildrenYes,payAnyoneToLookAfterChildrenNo,
        nameLookAfterChildren,howMuchLookAfterChildren, howOftenLookAfterChildren,
        relationToYouLookAfterChildren,relationToPersonLookAfterChildren,
        payAnyoneToLookAfterPersonYes,payAnyoneToLookAfterPersonNo,
        nameLookAfterPerson,howMuchLookAfterPerson, howOftenLookAfterPerson,
        relationToYouLookAfterPerson,relationToPersonLookAfterPerson) ->

  $("#" + haveExpensesForJobYes).on "click", ->
    $("#haveExpensesForJobWrap").slideDown 0
    $("#haveExpensesForJobWrap").css('display', "block")

  $("#" + haveExpensesForJobNo).on "click", ->
    $("#haveExpensesForJobWrap").slideUp 0, ->
      $("#" + whatExpensesForJob).val("")

  $("#" + payAnyoneToLookAfterChildrenYes).on "click", ->
    $("#payToLookAfterChildrenJobWrap").slideDown 0
    $("#payToLookAfterChildrenJobWrap").css('display', "block")

  $("#" + payAnyoneToLookAfterChildrenNo).on "click", ->
    $("#payToLookAfterChildrenJobWrap").slideUp 0, ->
      $("#" + nameLookAfterChildren).val("")
      $("#" + howMuchLookAfterChildren).val("")
      $("#" + howOftenLookAfterChildren).val("")
      $("#" + relationToYouLookAfterChildren).val("")
      $("#" + relationToPersonLookAfterChildren).val("")

  $("#" + payAnyoneToLookAfterPersonYes).on "click", ->
    $("#payToLookAfterPersonJobWrap").slideDown 0
    $("#payToLookAfterPersonJobWrap").css('display', "block")

  $("#" + payAnyoneToLookAfterPersonNo).on "click", ->
    $("#payToLookAfterPersonJobWrap").slideUp 0, ->
      $("#" + nameLookAfterPerson).val("")
      $("#" + howMuchLookAfterPerson).val("")
      $("#" + howOftenLookAfterPerson).val("")
      $("#" + relationToYouLookAfterPerson).val("")
      $("#" + relationToPersonLookAfterPerson).val("")