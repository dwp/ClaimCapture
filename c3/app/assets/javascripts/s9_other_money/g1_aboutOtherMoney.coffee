window.initEvents = (answerY, answerN, whoPaysYou, howMuch, howOften_frequency, howOften_frequency_other,
                     statSickPayY, statSickPayN,
                     statHowMuch, statHowOften_frequency, statHowOften_frequency_other, statEmployersName,
                     statEmployersAddress_lineOne, statEmployersAddress_lineTwo, statEmployersAddress_lineThree, statEmployersPostcode
                     otherPayY, otherPayN,
                     otherHowMuch, otherHowOften_frequency, otherHowOften_frequency_other, otherEmployersName,
                     otherEmployersAddress_lineOne, otherEmployersAddress_lineTwo, otherEmployersAddress_lineThree, otherEmployersPostcode) ->

  if not $("#" + answerY).prop 'checked'
    hideOtherPayWrap()

  if not $("#" + statSickPayY).prop 'checked'
    hideSickPayWrap()

  if not $("#" + otherPayY).prop 'checked'
    hideOtherStatPayWrap()


  $("#" + answerY).on "click", ->
    showOtherPayWrap()

  $("#" + answerN).on "click", ->
    hideOtherPayWrap()

  $("#" + statSickPayY).on "click", ->
    showSickPayWrap()

  $("#" + statSickPayN).on "click", ->
    hideSickPayWrap()

  $("#" + otherPayY).on "click", ->
    showOtherStatPayWrap()

  $("#" + otherPayN).on "click", ->
    hideOtherStatPayWrap()





showOtherStatPayWrap = ->
  $("#otherStatPayWrap").slideDown(0)

hideOtherStatPayWrap = ->
  $("#otherStatPayWrap").slideUp({duration:0,completed: ->
    $("#"+otherHowMuch).val("")
    $("#"+otherHowOften_frequency).val("")
    $("#"+otherHowOften_frequency_other).val("")
    $("#"+otherEmployersName).val("")
    $("#"+otherEmployersAddress_lineOne).val("")
    $("#"+otherEmployersAddress_lineTwo).val("")
    $("#"+otherEmployersAddress_lineThree).val("")
    $("#"+otherEmployersPostcode).val("")
  })

showSickPayWrap = ->
  $("#sickPayWrap").slideDown 0

hideSickPayWrap = ->
  $("#sickPayWrap").slideUp({duration:0,completed: ->
    $("#"+statHowMuch).val("")
    $("#"+statHowOften_frequency).val("")
    $("#"+statHowOften_frequency_other).val("")
    $("#"+statEmployersName).val("")
    $("#"+statEmployersAddress_lineOne).val("")
    $("#"+statEmployersAddress_lineTwo).val("")
    $("#"+statEmployersAddress_lineThree).val("")
    $("#"+statEmployersPostcode).val("")
  })

showOtherPayWrap = ->
  $("#otherPayWrap").slideDown(0)

hideOtherPayWrap = ->
  $("#otherPayWrap").slideUp({duration:0,completed: ->
    $("#"+whoPaysYou).val("")
    $("#"+howMuch).val("")
    $("#"+howOften_frequency).val("")
    $("#"+howOften_frequency_other).val("")
  })