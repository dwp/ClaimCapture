window.emailInit = (wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) ->

  if not $("#" + wantsEmailContactY).prop('checked')
    hideEmail(email, emailConfirmation)

  $("#" + wantsEmailContactY).on "click", ->
    showEmail()

  $("#" + wantsEmailContactN).on "click", ->
    hideEmail(email, emailConfirmation)

showEmail = ->
  $("#emailWrap").slideDown 0

hideEmail = (email, emailConfirmation) ->
  $("#emailWrap").slideUp 0, ->
    $("#" + email).val("")
    $("#" + emailConfirmation).val("")

