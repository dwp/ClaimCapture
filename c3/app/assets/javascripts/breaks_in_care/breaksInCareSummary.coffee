
window.initSummaryEvents = (o) ->
  setVisibility(o)

  $("#"+o.breakHospital).on "click", ->
    setVisibility(o)
  $("#"+o.breakCareHome).on "click", ->
    setVisibility(o)
  $("#"+o.breakOtherYes).on "click", ->
    setVisibility(o)
  $("#"+o.breakOtherNo).on "click", ->
    setVisibility(o)


# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = (o) ->
  # hide / bind delete prompts
  $(".delete-confirm").slideUp(0).attr 'aria-hidden', 'true'
  $(".deleterow").on "click", ->
    $(".delete-confirm").slideUp(0).attr 'aria-hidden', 'true'
    $(this).closest("tr").next().slideDown(0).attr 'aria-hidden', 'false'

  $(".noDelete").on "click", ->
    $(".delete-confirm").slideUp(0).attr 'aria-hidden', 'true'

  $(".yesDelete").on "click", ->
    iterationid=$(this).closest("tr").attr("iterationid")
    $("#" +o.deleteId).val(iterationid)
    $("#delete-break-form").submit()

  $(".changerow").on "click", ->
    href=$(this).attr("href")
    window.location.href = href

  if goToPreview(o)
    $(".form-steps button.button").text(o.textPreview)
  else
    $(".form-steps button.button").text(o.textNext)

goToPreview = (o) ->
  hospitalChecked=$("#"+o.breakHospital).is ":checked"
  careHomeChecked=$("#"+o.breakCareHome).is ":checked"
  otherYesSelected=$("#"+o.breakOtherYes).is ":checked"

  if (o.previewEnabled == "true" && (!hospitalChecked) && (!careHomeChecked) && (!otherYesSelected)) then true
  else false
