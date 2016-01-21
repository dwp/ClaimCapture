window.emailInit = (wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) ->
  if $("#" + wantsEmailContactY).prop('checked')
    showEmail()
  else if $("#" + wantsEmailContactN).prop('checked')
    hideEmail(email,emailConfirmation)
  else
    resetEmail()

  $("#" + wantsEmailContactY).on "click", ->
    showEmail()

  $("#" + wantsEmailContactN).on "click", ->
    hideEmail(email,emailConfirmation)

resetEmail = ->
  $("#emailWrap").slideUp(0).attr 'aria-hidden', 'true'
  $("#emailYesHelper").slideUp(0).attr 'aria-hidden', 'true'
  $("#emailNoHelper").slideUp(0).attr 'aria-hidden', 'true'

showEmail = ->
  $("#emailWrap").slideDown(0).attr 'aria-hidden', 'false'
  $("#emailYesHelper").slideDown(0).attr 'aria-hidden', 'false'
  $("#emailNoHelper").slideUp(0).attr 'aria-hidden', 'true'

hideEmail = (email, emailConfirmation) ->
  $("#" + email).val("")
  $("#" + emailConfirmation).val("")
  $("#emailWrap").slideUp(0).attr 'aria-hidden', 'true'
  $("#emailYesHelper").slideUp(0).attr 'aria-hidden', 'true'
  $("#emailNoHelper").slideDown(0).attr 'aria-hidden', 'false'



