$ ->
  $("#answer_no").on "click", ->
    $("#hasBreaksNoMessage").slideDown 500

  $("#answer_yes").on "click", ->
    $("#hasBreaksNoMessage").slideUp 500