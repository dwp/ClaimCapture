window.emailInit = (wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) ->

  if not $("#" + wantsEmailContactY).prop('checked')
    hideEmail(email, emailConfirmation)

  $("#" + wantsEmailContactY).on "click", ->
    showEmail()

  $("#" + wantsEmailContactN).on "click", ->
    hideEmail(email, emailConfirmation)

showEmail = ->
  $("#emailWrap").slideDown(0).attr 'aria-hidden', 'false'

hideEmail = (email, emailConfirmation) ->
	emptyEmail = ->
	    $("#" + email).val("")
	    $("#" + emailConfirmation).val("")
  $("#emailWrap").slideUp(0, emptyEmail).attr 'aria-hidden', 'true', ->

