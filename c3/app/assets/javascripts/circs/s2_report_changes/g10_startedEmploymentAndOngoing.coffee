window.haventBeenPaidYet = (haventBeenPaidYet) ->
  $("#" + haventBeenPaidYet).on "click", ->
    if $("#" + haventBeenPaidYet).is(":checked")
      $("#haventBeenPaidYetWrap").slideDown 500
      $("#haventBeenPaidYetWrap").css('display', "block")
    else
      $("#haventBeenPaidYetWrap").slideUp 500
