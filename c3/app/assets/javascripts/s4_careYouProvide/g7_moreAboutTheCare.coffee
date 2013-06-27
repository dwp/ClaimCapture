window.initEvents = (spent35HoursCaringBeforeClaimY,spent35HoursCaringBeforeClaimN,day,month,year)->
  $("#"+spent35HoursCaringBeforeClaimY).on "click", ->
    $("#careStartDateWrap").slideDown

  $("#"+spent35HoursCaringBeforeClaimN).on "click", ->
    $("#careStartDateWrap").slideUp, ->
      $("#"+day).val("")
      $("#"+month).val("")
      $("#"+year).val("")
