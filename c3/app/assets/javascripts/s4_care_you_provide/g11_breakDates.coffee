$ ->
    startPicker = $("#start.datepicker").pickadate().pickadate("picker")
    endPicker = $("#end.datepicker").pickadate().pickadate("picker")

    startPicker.on "close", (event) ->
        startDate = $("input[name='start.hidden']").val()
        console.log "Set start date to ", startDate
        endPicker.set "min", new Date(startDate)
        endPicker.set "highlight", new Date(startDate)

    endPicker.on "close", (event) ->
        endDate = $("input[name='end.hidden']").val()
        console.log "Set end date to ", endDate
        startPicker.set "max", new Date(endDate)