window.initEvents = (educationY, educationN,
                     courseTitle, nameOfSchoolCollegeOrUniversity,nameOfMainTeacherOrTutor,
                     courseContactNumber,startDateDay, startDateMonth, startDateYear,
                     expectedEndDateDay, expectedEndDateMonth, expectedEndDateYear) ->
  $("#" + educationY).on "click", ->
    $("#educationWrap").slideDown 500
    $("#educationWrap").css('display', "block")

  $("#" + educationN).on "click", ->
    $("#educationWrap").slideUp 500, ->
      $("#" + courseTitle).val("")
      $("#" + nameOfSchoolCollegeOrUniversity).val("")
      $("#" + nameOfMainTeacherOrTutor).val("")
      $("#" + courseContactNumber).val("")
      $("#" + startDateDay).val("")
      $("#" + startDateMonth).val("")
      $("#" + startDateYear).val("")
      $("#" + expectedEndDateDay).val("")
      $("#" + expectedEndDateMonth).val("")
      $("#" + expectedEndDateYear).val("")
