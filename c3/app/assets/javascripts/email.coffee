window.emailInit = (wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) ->

  if not $("#" + wantsEmailContactY).prop('checked')
    hideEmail(email, emailConfirmation)

  $("#" + wantsEmailContactY).on "click", ->
    showEmail()

  $("#" + wantsEmailContactN).on "click", ->
    hideEmail(email, emailConfirmation)

showEmail = ->
  $("#emailWrap").slideDown(0).attr 'aria-hidden', 'false'
  $("#emailYesHelper").slideDown(0).attr 'aria-hidden', 'false', ->
  $("#emailNoHelper").slideUp(0).attr 'aria-hidden', 'true', ->

hideEmail = (email, emailConfirmation) ->
  $("#" + email).val("")
  $("#" + emailConfirmation).val("")
  $("#emailWrap").slideUp(0).attr 'aria-hidden', 'true', ->
  $("#emailYesHelper").slideUp(0).attr 'aria-hidden', 'true', ->
  $("#emailNoHelper").slideDown(0).attr 'aria-hidden', 'false'

