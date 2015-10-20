window.initEvents = (o) ->

  if not $("#" + o.answerY).prop 'checked'
    hideOtherPayWrap(o)

  if not $("#" + o.statSickPayY).prop 'checked'
    hideSickPayWrap(o)

  if not $("#" + o.otherPayY).prop 'checked'
    hideOtherStatPayWrap(o)

  $("#" + o.answerY).on "click", ->
    showOtherPayWrap()

  $("#" + o.answerN).on "click", ->
    hideOtherPayWrap(o)

  $("#" + o.statSickPayY).on "click", ->
    showSickPayWrap()

  $("#" + o.statSickPayN).on "click", ->
    hideSickPayWrap(o)

  $("#" + o.otherPayY).on "click", ->
    showOtherStatPayWrap()

  $("#" + o.otherPayN).on "click", ->
    hideOtherStatPayWrap(o)


showOtherStatPayWrap = ->
  $("#otherStatPayWrap").slideDown(0).attr 'aria-hidden', 'false'

hideOtherStatPayWrap = (o) ->
  emptyOtherStatPay = ->
    $("#"+o.otherHowMuch).val("")
    $("#"+o.otherHowOften_frequency).val("")
    $("#"+o.otherHowOften_frequency_other).val("")
    $("#"+o.otherEmployersName).val("")
    $("#"+o.otherEmployersAddress_lineOne).val("")
    $("#"+o.otherEmployersAddress_lineTwo).val("")
    $("#"+o.otherEmployersAddress_lineThree).val("")
    $("#"+o.otherEmployersPostcode).val("")

  $("#otherStatPayWrap").slideUp(0, emptyOtherStatPay).attr 'aria-hidden', 'true'
  $("#otherStatPayWrap input").val("")

showSickPayWrap = ->
  $("#sickPayWrap").slideDown(0).attr 'aria-hidden', 'false'

hideSickPayWrap = (o) ->
  emptyStatPay = ->
    $("#" + o.statHowMuch).val("")
    $("#" + o.statHowOften_frequency).val("")
    $("#" + o.statHowOften_frequency_other).val("")
    $("#" + o.statEmployersName).val("")
    $("#" + o.statEmployersAddress_lineOne).val("")
    $("#" + o.statEmployersAddress_lineTwo).val("")
    $("#" + o.statEmployersAddress_lineThree).val("")
    $("#" + o.statEmployersPostcode).val("")

  $("#sickPayWrap").slideUp(0, emptyStatPay).attr 'aria-hidden', 'true'
  $("#sickPayWrap input").val("")

showOtherPayWrap = ->
  $("#otherPayWrap").slideDown(0).attr 'aria-hidden', 'false'

hideOtherPayWrap = (o) ->
  emptyOtherPay = ->
    $("#" + o.howOften_frequency_other).val("")
    $("#" + o.howOften_frequency).val("")
    $("#" + o.whoPaysYou).val("")
    $("#" + o.howMuch).val("")

  $("#otherPayWrap").slideUp(0, emptyOtherPay).attr 'aria-hidden', 'true'
  $("#otherPayWrap input").val("")