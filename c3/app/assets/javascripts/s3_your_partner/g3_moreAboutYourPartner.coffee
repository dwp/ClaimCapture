window.initEvents = (startedLivingTogetherY, startedLivingTogetherN, separatedFromPartnerIDYes, separatedFromPartnerIDNo) ->
  $("#" + startedLivingTogetherY).on "click", ->
    $("#startedLivingTogetherInfo").slideDown 500
    $("#startedLivingTogetherInfo").css('display', "block")

  $("#" + startedLivingTogetherN).on "click", ->
    $("#startedLivingTogetherInfo").slideUp 500
    $('#startedLivingTogether_date_day').val('').trigger('liszt:updated')
    $('#startedLivingTogether_date_month').val('').trigger('liszt:updated')
    $('#startedLivingTogether_date_year').val("")

  $("#" + separatedFromPartnerIDYes).on "click", ->
    $("#separatedInfo").slideDown 500
    $("#separatedInfo").css('display', "block")
    
  $("#" + separatedFromPartnerIDNo).on "click", ->
    $("#separatedInfo").slideUp 500
    $('#separated_date_day').val('').trigger('liszt:updated')
    $('#separated_date_month').val('').trigger('liszt:updated')
    $('#separated_date_year').val("")
