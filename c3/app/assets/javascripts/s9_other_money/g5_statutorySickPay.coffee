window.initEvents = (haveYouHadAnyStatutorySickPayY, haveYouHadAnyStatutorySickPayN, howMuch, howOften_frequency, howOften_frequency_other, employersName, employersAddress_lineOne, employersAddress_lineTwo, employersAddress_lineThree, employersPostcode) ->
  $("#" + haveYouHadAnyStatutorySickPayY).on "click", ->
    $("#sickPayWrap").slideDown 500
    $("#sickPayWrap").css('display', "block")
    
  $("#" + haveYouHadAnyStatutorySickPayN).on "click", ->
    $("#sickPayWrap").slideUp ->
      $("#"+howMuch).val("")
      $("#"+howOften_frequency).val("")
      $("#"+howOften_frequency_other).val("")
      $("#"+employersName).val("")
      $("#"+employersAddress_lineOne).val("")
      $("#"+employersAddress_lineTwo).val("")
      $("#"+employersAddress_lineThree).val("")
      $("#"+employersPostcode).val("")