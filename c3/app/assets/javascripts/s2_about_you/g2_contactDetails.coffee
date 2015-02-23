window.initEvents = (wantsEmailContactY, wantsEmailContactN, email, emailConfirmation) ->

  if not $("#" + wantsEmailContactY).prop('checked')
    hide(email, emailConfirmation)

  $("#" + wantsEmailContactY).on "click", ->
    show()

  $("#" + wantsEmailContactN).on "click", ->
    hide(email, emailConfirmation)

show = ->
  $("#emailWrap").slideDown 0

hide = (email, emailConfirmation) ->
  $("#emailWrap").slideUp 0, ->
    $("#" + email).val("")
    $("#" + emailConfirmation).val("")

