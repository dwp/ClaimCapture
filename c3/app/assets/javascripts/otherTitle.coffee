window.otherTitle = (title) ->

  hideShow(title)
  $('#'+title+" input").change ->
    hideShow(title)

hideShow = (title) ->
  other = $('#otherTitle')
  titleVal = $("input[name="+title+"]:checked").val()

  if titleVal is 'Other'
    other.show()
  else
    other.hide().find("input").val("");