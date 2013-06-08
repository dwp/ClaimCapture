$ ->
    scroll = $("input[type=submit]").offset().top + 2 * $("input[type=submit]").height() - $(window).height()

    $('html, body').animate({
        scrollTop: scroll
    }, 1000)