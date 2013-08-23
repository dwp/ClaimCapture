window.initEvents = (howOften_frequency, howOften_frequency_wrap) ->
  $("#" + howOften_frequency).on "change", ->
    selected = $("#" + howOften_frequency + " option").filter(':selected').text()
    if selected is "Other"
      $("#" + howOften_frequency_wrap).slideDown()
      $("#" + howOften_frequency_wrap).css('display', "block")
    else 
      $("#" + howOften_frequency_wrap).slideUp()