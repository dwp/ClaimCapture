executeEvent = (selector, maxChars) ->
    elem = $(selector)
    helper = elem.parent().find(".countdown")
    text = helper.html()
    num = maxChars - elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").length
    if ((text != undefined) && (text.length > 0))
      helper.html(text.replace /-?([0-9]+)/,num)

window.areaCounter = (textarea) ->
    selector = "#" +textarea.selector

    if ($(selector).length > 0)
        $(selector).on "click", -> executeEvent("#" + textarea.selector,textarea.maxChars)
        $(selector).on "blur", -> executeEvent("#" + textarea.selector,textarea.maxChars)
        $(selector).on "keyup", -> executeEvent("#" + textarea.selector,textarea.maxChars)