$ ->
  $("#breaks").hide() if $("tbody").children().length is 0

  $("tbody").on "click", "input[value='Edit']", ->
    tr = $(this).closest("tr")
    window.location.href = "/careYouProvide/breaks/" + tr.attr("id")

  $("tbody").on "click", "input[value='Delete']", ->
    tr = $(this).closest("tr")
    tbody = $(this).closest("tbody")

    $.ajax
      type: "DELETE"
      url: "/careYouProvide/breaksInCare/" + tr.attr("id")

      success: ->
        element = undefined

        if tbody.children().length is 1
          element = $("#breaks")
        else
          element = tr.children("td")

        element.animate(
          padding: 0
          margin: 0
        ).wrapInner("<div />").children().slideUp ->
          tr.remove()


      error: ->
        alert "Failed to delete break - Contact Support"