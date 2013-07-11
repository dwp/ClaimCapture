chars = 0
executeEvent = (elem)->
  helper = elem.parent().find(".helper")
  text = helper.html()
  num = chars - elem.val().length

  if num < 0
    helper.addClass("warning")
  else if  helper.hasClass("warning")
    helper.removeClass("warning")

  helper.html(text.replace /-?([0-9]+)/,num)




window.initEvents = (anythingElse,maxChars) ->
  chars = maxChars
  selector = "#" + anythingElse
  $(selector).on "click", -> executeEvent($(selector))
  $(selector).on "blur", -> executeEvent($(selector))
  $(selector).on "keyup", -> executeEvent($(selector))