window.initEvents = (o) ->
  setVisibility(o)

  $("#"+o.paidY).on "click", ->
    setVisibility(o)
  $("#"+o.paidN).on "click", ->
    setVisibility(o)
  $("#"+o.employerOwesYouMoneyY).on "click", ->
    setVisibility(o)
  $("#"+o.employerOwesYouMoneyN).on "click", ->
    setVisibility(o)

# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (o) ->
  # Update character counters
  $("#"+o.payIntoPensionText).trigger("blur")
  $("#"+o.whatWasIncluded ).trigger("blur")

  if $("#"+o.paidY).prop 'checked'
    setLabel(o.howmuchQL, o.howmuchQ)
    setLabel(o.paydateQL, o.paydateQ)
    setLabel(o.whatWasIncludedQL, o.whatWasIncludedQ)
    setLabel(o.howOftenQL, o.howOftenQ)
  else if $("#"+o.paidN).prop 'checked'
    setLabel(o.howmuchQL, o.howmuchExpectQ)
    setLabel(o.paydateQL, o.paydateExpectQ)
    setLabel(o.whatWasIncludedQL, o.whatWasIncludedExpectQ)
    setLabel(o.howOftenQL, o.howOftenExpectQ)
  if ( ( $("#"+o.paidY).prop 'checked' ) || ( o.pastpresentfuture != "future" && $("#"+o.paidN).prop 'checked' ) )
    showWrapper(o.howmuchWrapper)
    showWrapper(o.paydateWrapper)
    showWrapper(o.whatWasIncludedWrapper)
    showWrapper(o.howOftenWrapper)
  else
    hideWrapper(o.howmuchWrapper)
    hideWrapper(o.paydateWrapper)
    hideWrapper(o.whatWasIncludedWrapper)
    hideWrapper(o.howOftenWrapper)

  if $("#"+o.employerOwesYouMoneyY).prop 'checked'
    showWrapper(o.employerOwesYouMoneyWrapper)
  else
    hideWrapper(o.employerOwesYouMoneyWrapper)


# Common functions
setLabel = (id,text) ->
  $("#" + id).html(text)

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