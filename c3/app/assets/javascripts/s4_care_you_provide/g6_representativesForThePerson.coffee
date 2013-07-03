window.initEvents = (actForPersonY, actForPersonN, someoneElseActForPersonY, someoneElseActForPersonN, someoneElseActAsID, someoneElseFullNameID) ->
  $("#" +actForPersonY).on "click", ->
    $("#actWrap").slideDown 500
    $("#actWrap").css('display',"block")

  $("#" +actForPersonN).on "click", ->
    $("#actWrap").slideUp 500, -> $("#actAs").val("")


  $("#" +someoneElseActForPersonY).on "click", ->
    $("#someoneElseWrap").slideDown 500
    $("#someoneElseWrap").css('display',"block")

  $("#" +someoneElseActForPersonN).on "click", ->
    $("#someoneElseWrap").slideUp 500, ->
      $("#" + someoneElseActAsID).val("")
      $("#" + someoneElseFullNameID).val("")
