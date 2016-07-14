window.init = ( otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails ) ->
  setVisibility(otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails )

  $("#" + otherCarer_yes).on "click", ->
    setVisibility(otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails )
  $("#" + otherCarer_no).on "click", ->
    setVisibility(otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails )

  $("#" + otherCarerUc_yes).on "click", ->
    setVisibility(otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails )
  $("#" + otherCarerUc_no).on "click", ->
    setVisibility(otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails )

# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (otherCarer_yes, otherCarer_no, otherCarerUcWrap, otherCarerUc_yes, otherCarerUc_no, otherCarerUcDetailsWrap, otherCarerUcDetails ) ->
  # Update the character counter in case its been shown / hidden and maybe reset
  $("#" + otherCarerUcDetails).trigger("blur")

  if($("#" + otherCarer_yes).prop('checked'))
    showWrapper(otherCarerUcWrap)
  else
    hideWrapper(otherCarerUcWrap)

  if($("#" + otherCarerUc_yes).prop('checked'))
    showWrapper(otherCarerUcDetailsWrap)
  else
    hideWrapper(otherCarerUcDetailsWrap)


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