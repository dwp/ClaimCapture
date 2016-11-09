window.initPayFrequency = (o) ->
  setVisibility(o)

  $("#"+o.paidY).on "click", ->
    setVisibility(o)
  $("#"+o.paidN).on "click", ->
    setVisibility(o)
  $("#"+o.howOftenFrequency).on "change", ->
    setVisibility(o)

# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (o) ->
  $("#" + o.moreinfo ).trigger("blur")

  if o.pastpresentfuture == "future"
    if $("#"+o.paidY).prop 'checked'
      showWrapper(o.howOftenWrapper)
    else
      hideWrapper(o.howOftenWrapper)

  if $("#"+o.paidN).prop 'checked'
    expectedLabels="Expect"
  else
    expectedLabels=""

  selectedFrequency=$("#"+o.howOftenFrequency).val()
  textToUse = switch (selectedFrequency + expectedLabels)
    when "Weekly" then o.weeklyQ
    when "WeeklyExpect" then o.weeklyExpectQ
    when "Fortnightly" then o.fortnightlyQ
    when "FortnightlyExpect" then o.fortnightlyExpectQ
    when "Four-Weekly" then o.fourWeeklyQ
    when "Four-WeeklyExpect" then o.fourWeeklyExpectQ
    when "Monthly" then o.monthlyQ
    when "MonthlyExpect" then o.monthlyExpectQ
    when "Other" then o.otherQ
    when "OtherExpect" then o.otherExpectQ
    else "HIDE"

  if(selectedFrequency == "Monthly")
    showWrapper(o.monthlyPayDayWrapper)
  else
    hideWrapper(o.monthlyPayDayWrapper)

  if textToUse == "HIDE"
    hideWrapper(o.sameAmountWrapper)
  else
    showWrapper(o.sameAmountWrapper)
    setLabel(o.sameAmount, textToUse)

  if $("#"+o.paidN).prop 'checked'
    setLabel(o.monthlyPayDay, o.monthlyPayDayExpectQ)
  else
    setLabel(o.monthlyPayDay, o.monthlyPayDayQ)


# Common functions
####################
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
  $("#" + wrapper).find("select").each(clearInput)

# If we want to also clear the validation error when item is hidden ?
# $("#" + wrapper).find(".validation-error").removeClass("validation-error")
# $("#" + wrapper).find(".validation-message").remove()
clearInput = ->
  if( $(this).attr("type") == "radio" )
    $(this).prop('checked', false)
    $(this).parent().removeClass("selected")
  else
    $(this).val("")