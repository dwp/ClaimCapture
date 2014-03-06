window.initEvents = (currentlyPaidIntoBankY, currentlyPaidIntoBankN, text1, text2) ->
  $("#" + currentlyPaidIntoBankY).on "click", ->
    $("#currentlyPaidIntoBankWrap2").slideUp 500, ->
      $("#" + text2).val("")
      $("#currentlyPaidIntoBankWrap1").slideDown 500
      $("#currentlyPaidIntoBankWrap1").css('display', "block")

  $("#" + currentlyPaidIntoBankN).on "click", ->
    $("#currentlyPaidIntoBankWrap1").slideUp 500, ->
      $("#" + text1).val("")
      $("#currentlyPaidIntoBankWrap2").slideDown 500
      $("#currentlyPaidIntoBankWrap2").css('display', "block")
