window.emailInit = (wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) ->

  if not $("#" + wantsEmailContactY).prop('checked')
    hideEmail(email, emailConfirmation, wantsEmailContactN)

  $("#" + wantsEmailContactY).on "click", ->
    showEmail()

  $("#" + wantsEmailContactN).on "click", ->
    hideEmail(email, emailConfirmation, wantsEmailContactN)

showEmail = ->
  $("#emailWrap").slideDown(0).attr 'aria-hidden', 'false'
  $("#emailYesHelper").slideDown(0).attr 'aria-hidden', 'false', ->
  $("#emailNoHelper").slideUp(0).attr 'aria-hidden', 'true', ->

hideEmail = (email, emailConfirmation, wantsEmailContactN) ->
  $("#" + email).val("")
  $("#" + emailConfirmation).val("")
  $("#emailWrap").slideUp(0).attr 'aria-hidden', 'true', ->
  $("#emailYesHelper").slideUp(0).attr 'aria-hidden', 'true', ->
  if not $("#" + wantsEmailContactN).prop('checked')
    $("#emailNoHelper").slideUp(0).attr 'aria-hidden', 'true', ->
  else
    $("#emailNoHelper").slideDown(0).attr 'aria-hidden', 'false', ->


