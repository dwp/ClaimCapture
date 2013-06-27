window.initEvents = (spent35HoursCaringBeforeClaimY,spent35HoursCaringBeforeClaimN,day,month,year)->
  $("#"+spent35HoursCaringBeforeClaimY).on "click", ->
    $("#careStartDateWrap").slideDown 500

  $("#"+spent35HoursCaringBeforeClaimN).on "click", ->
    $("#careStartDateWrap").slideUp 500, ->
      $("#"+day).val("")
      $("#"+month).val("")
      $("#"+year).val("")
