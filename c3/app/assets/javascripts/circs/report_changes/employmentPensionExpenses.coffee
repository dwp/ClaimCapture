window.initEvents = (o) ->
  setVisibility(o)

  $("#"+o.payIntoPensionY).on "click", ->
    setVisibility(o)
  $("#"+o.payIntoPensionN).on "click", ->
    setVisibility(o)
  $("#"+o.payForThingsY).on "click", ->
    setVisibility(o)
  $("#"+o.payForThingsN).on "click", ->
    setVisibility(o)
  $("#"+o.careCostsY).on "click", ->
    setVisibility(o)
  $("#"+o.careCostsN).on "click", ->
    setVisibility(o)

# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (o) ->
  if $("#"+o.payIntoPensionY).prop 'checked'
    showWrapper(o.payIntoPensionWrap)
  else
    hideWrapper(o.payIntoPensionWrap)

  if $("#"+o.payForThingsY).prop 'checked'
    showWrapper(o.payForThingsWrap)
  else
    hideWrapper(o.payForThingsWrap)

  if $("#"+o.careCostsY).prop 'checked'
    showWrapper(o.careCostsWrap)
  else
    hideWrapper(o.careCostsWrap)

  # Update character counters
  $("#"+o.payIntoPensionText).trigger("blur")
  $("#"+o.payForThingsText).trigger("blur")
  $("#"+o.careCostsText).trigger("blur")
  $("#"+o.moreAboutChangesText).trigger("blur")


# Common functions
showWrapper = (wrapper) ->
  $("#" + wrapper).slideDown(0).attr 'aria-hidden', 'false'

hideWrapper = (wrapper)->
  clearDownStreamInputs(wrapper)
  $("#" + wrapper).slideUp(0).attr 'aria-hidden', 'true'

clearDownStreamInputs = (wrapper)->
  $("#" + wrapper).find("input").each(clearInput)
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