$ = jQuery

$.fn.extend
  trSlideUp: (callback) ->
    $row = this
    $tds = @find("td")
    highestTd = @getTallestTd $tds

    # Animate the opacity of the row so it fades away but doesn't cause a reflow
    $row.animate
      opacity: 0,
      300, ->
      # All tds are now invisible and we only need to slide the tallest one so for all but the tallest td empty all other tds and remove their padding.
      $tds.each (i, td) ->
        unless this == highestTd
          $(this).empty()
          $(this).css "padding", "0"
      $td = $(highestTd)

      # Create a wrapper to perform slide on
      $wrapper = $("<div/>")

      # Transfer any padding on td to wrapper
      $wrapper.css $td.css("padding")
      $td.css "padding", "0"

      # Add the wrapper
      $td.wrapInner $wrapper

      # Perform slide on wrapper
      $td.children("div").slideUp 300, =>
        $row.remove()
        callback() if callback

  #-

  getTallestTd: ($tds) ->
    index = -1
    height = 0

    $tds.each (i, td) ->
      if $(td).height() > height
        index = i
        height = $(td).height()
    $tds.get(index)