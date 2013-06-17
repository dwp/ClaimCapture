$ ->
  $("#currentlyLivingInUK_yes").on "click", ->
    $("#arrivedInUK").parent().parent().slideUp 500

  $("#currentlyLivingInUK_no").on "click", ->
    $("#arrivedInUK").parent().parent().slideDown 500

  $("#planToGoBack_yes").on "click", ->
    $("#whenPlanToGoBack").parent().parent().slideUp 500

  $("#planToGoBack_no").on "click", ->
    $("#whenPlanToGoBack").parent().parent().slideDown 500