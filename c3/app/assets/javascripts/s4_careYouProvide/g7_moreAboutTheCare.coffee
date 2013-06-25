$ ->
  $("#spent35HoursCaringBeforeClaim_yes").on "click", ->
    $("#careStartDateWrap").slideDown 500

  $("#spent35HoursCaringBeforeClaim_no").on "click", ->
    $("#careStartDateWrap").slideUp 500
