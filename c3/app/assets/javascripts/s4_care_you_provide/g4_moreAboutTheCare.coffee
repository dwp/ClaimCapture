window.initEvents = (spent35HoursCaringBeforeClaimY, spent35HoursCaringBeforeClaimN, day, month, year) ->
  $("#" + spent35HoursCaringBeforeClaimY).on "click", ->
    $("#careStartDateWrap").slideDown 0
    $("#careStartDateWrap").css('display',"block")

  $("#" + spent35HoursCaringBeforeClaimN).on "click", ->
    $("#careStartDateWrap").slideUp 0
    $("#" + day).val("")
    $("#" + month).val("")
    $("#" + year).val("")