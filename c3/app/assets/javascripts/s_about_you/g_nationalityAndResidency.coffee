window.initNationalityEvents = (nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap,
                                alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap,
                                liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap,
                                arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap
                                trip52weeksY, trip52weeksN, trip52weeksYesWrap,
                                tripDetails
                      ) ->
  setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )

  $("#" + nationalityBritish).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )
  $("#" + nationalityAnotherCountry).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )

  $("#" + alwaysLivedInUKY).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )
  $("#" + alwaysLivedInUKN).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )

  $("#" + liveInUKNowY).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )
  $("#" + liveInUKNowN).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )

  $("#" + arrivedInUKless).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )
  $("#" + arrivedInUKmore).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )

  $("#" + trip52weeksY).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )
  $("#" + trip52weeksN).on "click", ->
    setVisibility(nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap, alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap, liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap, arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap, trip52weeksY, trip52weeksN, trip52weeksYesWrap, tripDetails )


# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (nationalityBritish, nationalityAnotherCountry, actualNationality, actualNationalityWrap,
                 alwaysLivedInUKY, alwaysLivedInUKN, alwaysLivedInUKNoWrap,
                 liveInUKNowY, liveInUKNowN, liveInUKNowYesWrap,
                 arrivedInUKless, arrivedInUKmore, arrivedInUKRecentWrap,
                 trip52weeksY, trip52weeksN, trip52weeksYesWrap,
                  tripDetails) ->
  # Update the character counter in case its been shown / hidden and maybe reset
  $("#" + tripDetails).trigger("blur")

  if $("#" + nationalityAnotherCountry).prop('checked')
    showWrapper(actualNationalityWrap)
  else
    hideWrapper(actualNationalityWrap)

  if $("#" + alwaysLivedInUKN).prop('checked')
    showWrapper(alwaysLivedInUKNoWrap)
  else
    hideWrapper(alwaysLivedInUKNoWrap)

  if $("#" + liveInUKNowY).prop('checked')
    showWrapper(liveInUKNowYesWrap)
  else
    hideWrapper(liveInUKNowYesWrap)

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
  clearDownStreamTextAreas(wrapper)
  $("#" + wrapper).slideUp(0).attr 'aria-hidden', 'true'

clearDownStreamInputs = (wrapper)->
  $("#" + wrapper).find("input").each(clearInput)

clearDownStreamTextAreas = (wrapper)->
  $("#" + wrapper).find("textarea").each(clearInput)


# If we want to also clear the validation error when item is hidden ?
# $("#" + wrapper).find(".validation-error").removeClass("validation-error")
# $("#" + wrapper).find(".validation-message").remove()
clearInput = ->
  if( $(this).attr("type") == "radio" )
    $(this).prop('checked', false)
    $(this).parent().removeClass("selected")
  else
    $(this).val("")