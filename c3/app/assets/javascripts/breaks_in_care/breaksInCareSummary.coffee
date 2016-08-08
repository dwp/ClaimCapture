
window.initSummaryEvents = (other_yes, other_no, answerWrap) ->
  setVisibility(other_yes, other_no, answerWrap)
  $("#" + other_yes).on "click", ->
    setVisibility(other_yes, other_no, answerWrap)
  $("#" + other_no).on "click", ->
    setVisibility(other_yes, other_no, answerWrap)


# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (other_yes, other_no, answerWrap) ->
  console.log("set vis called")
  if $("#" + other_yes).prop('checked')
    showWrapper(answerWrap)
  else
    hideWrapper(answerWrap)


  # hide / bind delete prompts
  $(".delete-confirm").slideUp(0).attr 'aria-hidden', 'true'
  $(".deleterow").on "click", ->
    $(".delete-confirm").slideUp(0).attr 'aria-hidden', 'true'
    $(this).closest("tr").next().slideDown(0).attr 'aria-hidden', 'false'

  $(".noDelete").on "click", ->
    $(".delete-confirm").slideUp(0).attr 'aria-hidden', 'true'

  $(".yesDelete").on "click", ->
    iterationid=$(this).closest("tr").attr("iterationid")
    $("#deleteId").val(iterationid)
    $("#delete-break-form").submit()

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