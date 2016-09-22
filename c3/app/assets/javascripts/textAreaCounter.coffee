executeEvent = (selector, maxChars, type) ->
    elem = $(selector)
    if (type == "blur" || (type == "keyup" && elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").length > maxChars))
        checkValidCharacters(elem, maxChars)
    helper = elem.parent().find(".countdown")
    text = helper.html()
    num = maxChars - elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").length
    if ((text != undefined) && (text.length > 0))
      helper.html(text.replace /-?([0-9]+)/,num)

checkValidCharacters = (elem, maxChars) ->
    newElem = elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n")
    newMaxChars = maxChars
    if (newElem.lastIndexOf("\r\n") == maxChars-1)
        newMaxChars = maxChars-1
    elem.val(elem.val().replace(/\r(?!\n)|\n(?!\r)/g, "\r\n").substr(0, newMaxChars))

window.areaCounter = (textarea) ->
    selector = "#" + textarea.selector

    if ($(selector).length > 0)
        $(selector).on "click", -> executeEvent(selector, textarea.maxChars, "click")
        $(selector).on "blur", -> executeEvent(selector, textarea.maxChars, "blur")
        $(selector).on "keyup", -> executeEvent(selector, textarea.maxChars, "keyup")