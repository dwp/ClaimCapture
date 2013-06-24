$ ->
  $("#actForPerson_yes").on "click", ->
    $("#actWrap").slideDown 500
  $("#actForPerson_no").on "click", ->
    $("#actWrap").slideUp 500
    $("#actAs").val("")

  $("#someoneElseActForPerson_yes").on "click", ->
    $("#someoneElseWrap").slideDown 500
  $("#someoneElseActForPerson_no").on "click", ->
    $("#someoneElseWrap").slideUp 500
    $("#someoneElseActAs").val("")
    $("#someoneElseFullName").val("")


