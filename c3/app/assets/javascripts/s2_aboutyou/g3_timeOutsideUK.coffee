$ ->
  $("#currentlyLivingInUK_yes").on "click", ->
    $("#livingInUK").slideUp 500

  $("#currentlyLivingInUK_no").on "click", ->
    $("#livingInUK").slideDown 500

  $("#planToGoBack_yes").on "click", ->
    $("#planingToGoBack").slideDown 500

  $("#planToGoBack_no").on "click", ->
    $("#planingToGoBack").slideUp 500