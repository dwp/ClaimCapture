window.initEvents = (
        haveExpensesForJobYes, haveExpensesForJobNo, whatExpensesForJob,
        payAnyoneToLookAfterChildrenYes,payAnyoneToLookAfterChildrenNo,
        nameLookAfterChildren,howMuchLookAfterChildren, howOftenLookAfterChildren,
        relationToYouLookAfterChildren,relationToPersonLookAfterChildren,
        payAnyoneToLookAfterPersonYes,payAnyoneToLookAfterPersonNo,
        nameLookAfterPerson,howMuchLookAfterPerson, howOftenLookAfterPerson,
        relationToYouLookAfterPerson,relationToPersonLookAfterPerson) ->

  $("#" + haveExpensesForJobYes).on "click", ->
    $("#haveExpensesForJobWrap").slideDown 500
    $("#haveExpensesForJobWrap").css('display', "block")

  $("#" + haveExpensesForJobNo).on "click", ->
    $("#haveExpensesForJobWrap").slideUp 500, ->
      $("#" + whatExpensesForJob).val("")

  $("#" + payAnyoneToLookAfterChildrenYes).on "click", ->
    $("#payToLookAfterChildrenJobWrap").slideDown 500
    $("#payToLookAfterChildrenJobWrap").css('display', "block")

  $("#" + payAnyoneToLookAfterChildrenNo).on "click", ->
    $("#payToLookAfterChildrenJobWrap").slideUp 500, ->
      $("#" + nameLookAfterChildren).val("")
      $("#" + howMuchLookAfterChildren).val("")
      $("#" + howOftenLookAfterChildren).val("")
      $("#" + relationToYouLookAfterChildren).val("")
      $("#" + relationToPersonLookAfterChildren).val("")

  $("#" + payAnyoneToLookAfterPersonYes).on "click", ->
    $("#payToLookAfterPersonJobWrap").slideDown 500
    $("#payToLookAfterPersonJobWrap").css('display', "block")

  $("#" + payAnyoneToLookAfterPersonNo).on "click", ->
    $("#payToLookAfterPersonJobWrap").slideUp 500, ->
      $("#" + nameLookAfterPerson).val("")
      $("#" + howMuchLookAfterPerson).val("")
      $("#" + howOftenLookAfterPerson).val("")
      $("#" + relationToYouLookAfterPerson).val("")
      $("#" + relationToPersonLookAfterPerson).val("")