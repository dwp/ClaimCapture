
window.initSummaryEvents = () ->
  setVisibility()


# Called whenever anything changes ... we set up all the hidden items and reset / clear any that are hidden
setVisibility = () ->
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

  $(".changerow").on "click", ->
    href=$(this).attr("href")
    window.location.href = href
