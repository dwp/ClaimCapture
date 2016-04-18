window.initNationalityEvents = (nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap,
                                alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap,
                                liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap,
                                arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap
                                trip52weeksY, trip52weeksN, trip52weeksYesWrap
                      ) ->
  setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )

  $("#" + nationalityBritish).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )
  $("#" + nationalityAnotherCountry).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )

  $("#" + alwaysLivedInUKY).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )
  $("#" + alwaysLivedInUKN).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )

  $("#" + liveInUKNowY).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )
  $("#" + liveInUKNowN).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )

  $("#" + arrivedInUKless).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )
  $("#" + arrivedInUKmore).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )

  $("#" + trip52weeksY).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )
  $("#" + trip52weeksN).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap )


# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap,
                 alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap,
                 liveInUKNowY, liveInUKNowN, liveInUKNowNoWrap,
                 arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap,
                 trip52weeksY, trip52weeksN, trip52weeksYesWrap) ->
  if $("#" + nationalityAnotherCountry).prop('checked')
    showWrapper(actualNationalityWrap)
  else
    hideWrapper(actualNationalityWrap)

  if $("#" + alwaysLivedInUKN).prop('checked')
    showWrapper(alwaysLivedInUKNoWrap)
  else
    hideWrapper(alwaysLivedInUKNoWrap)

  if $("#" + liveInUKNowN).prop('checked')
    showWrapper(liveInUKNowNoWrap)
  else
    hideWrapper(liveInUKNowNoWrap)

  if $("#" + arrivedInUKless).prop('checked')
    showWrapper(arrivedInUKRecentWrap)
  else
    hideWrapper(arrivedInUKRecentWrap)

  if $("#" + trip52weeksY).prop('checked')
    showWrapper(trip52weeksYesWrap)
  else
    hideWrapper(trip52weeksYesWrap)



# Copied from SelfEmploymentDates.coffee
# We should put this somewhere common ?
showWrapper = (wrapper) ->
  $("#" + wrapper).slideDown(0).attr 'aria-hidden', 'false'

hideWrapper = (wrapper)->
  clearDownStreamInputs(wrapper)
  $("#" + wrapper).slideUp(0).attr 'aria-hidden', 'true'

clearDownStreamInputs = (wrapper)->
  $("#" + wrapper).find("input").each(clearInput)

# If we want to also clear the validation error when item is hidden ?
# $("#" + wrapper).find(".validation-error").removeClass("validation-error")
# $("#" + wrapper).find(".validation-message").remove()
clearInput = ->
  if( $(this).attr("type") == "radio" )
    $(this).prop('checked', false)
    $(this).parent().removeClass("selected")
  else
    $(this).val("")