window.initEvents = (answerY, answerN, howMuch, howOften_frequency, howOften_frequency_other, employersName, employersAddress_lineOne, employersAddress_lineTwo, employersAddress_lineThree, employersPostcode) ->
  $("#" + answerY).on "click", ->
    $("#otherPayWrap").slideDown()
    $("#otherPayWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#otherPayWrap").slideUp ->
      $("#"+howMuch).val("")
      $("#"+howOften_frequency).val("")
      $("#"+howOften_frequency_other).val("")
      $("#"+employersName).val("")
      $("#"+employersAddress_lineOne).val("")
      $("#"+employersAddress_lineTwo).val("")
      $("#"+employersAddress_lineThree).val("")
      $("#"+employersPostcode).val("")