package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import app.CircsBreaksWhereabouts
import scala.Predef._
import models.DayMonthYear
import controllers.Mappings
import models.yesNo.{YesNoDontKnowWithDates, YesNoWithDateTimeAndText, RadioWithText}


class G7BreaksInCareFormSpec extends Specification with Tags {

  "Report a change in your circumstances - Breaks from caring Form" should {

    val recentBreakFromCaringDateDay = "10"
    val recentBreakFromCaringDateMonth = "11"
    val recentBreakFromCaringDateYear = "2012"

    val breakEndedEndDateDay = "10"
    val breakEndedEndDateMonth = "10"
    val breakEndedEndDateYear = "2002"

    val expectStartCaringDateDay = "11"
    val expectStartCaringDateMonth = "11"
    val expectStartCaringDateYear = "2001"

    val expectStartCaringPermanentBreakDateDay = "9"
    val expectStartCaringPermanentBreakDateMonth = "9"
    val expectStartCaringPermanentBreakDateYear = "2002"

    val breaksInCareStartTime = "10 am"
    val yes = Mappings.yes
    val no = Mappings.no

    "map data into case class" in {
       G7BreaksInCare.form.bind(
          Map(
            "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
            "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
            "breaksInCareStartDate.year" -> recentBreakFromCaringDateYear,
            "breaksInCareStartTime" -> breaksInCareStartTime,
            "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
            "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.SomewhereElse,
            "whereYouBreaksInCare.text" -> "Other reasons",
            "breakEnded.answer" -> yes,
            "breakEnded.endDate.day" -> breakEndedEndDateDay,
            "breakEnded.endDate.month" -> breakEndedEndDateMonth,
            "breakEnded.endDate.year" -> breakEndedEndDateYear,
            "breakEnded.endTime" -> breaksInCareStartTime,
            "expectStartCaring.answer" -> yes,
            "expectStartCaring.expectStartCaringDate.day" -> expectStartCaringDateDay,
            "expectStartCaring.expectStartCaringDate.month" -> expectStartCaringDateMonth,
            "expectStartCaring.expectStartCaringDate.year" -> expectStartCaringDateYear,
            "expectStartCaring.permanentBreakDate.day" -> expectStartCaringPermanentBreakDateDay,
            "expectStartCaring.permanentBreakDate.month" -> expectStartCaringPermanentBreakDateMonth,
            "expectStartCaring.permanentBreakDate.year" -> expectStartCaringPermanentBreakDateYear,
            "medicalCareDuringBreak" -> yes,
            "moreAboutChanges" -> "I do not have changes"
          )
       ).fold(
         formWithErrors => "This mapping should not happen." must equalTo("Error"),
         form => {
           form.breaksInCareStartDate must equalTo(DayMonthYear(recentBreakFromCaringDateDay.toInt, recentBreakFromCaringDateMonth.toInt, recentBreakFromCaringDateYear.toInt))
           form.breaksInCareStartTime must equalTo(Some(breaksInCareStartTime))
           form.wherePersonBreaksInCare must equalTo(RadioWithText(answer = CircsBreaksWhereabouts.Hospital))
           form.whereYouBreaksInCare must equalTo(RadioWithText(CircsBreaksWhereabouts.SomewhereElse, Some("Other reasons")))
           form.breakEnded must equalTo(YesNoWithDateTimeAndText(yes, Some(DayMonthYear(breakEndedEndDateDay.toInt,breakEndedEndDateMonth.toInt,breakEndedEndDateYear.toInt)),Some(breaksInCareStartTime)))
           form.expectStartCaring must equalTo(YesNoDontKnowWithDates(Some(yes),
                                               Some(DayMonthYear(expectStartCaringDateDay.toInt,expectStartCaringDateMonth.toInt, expectStartCaringDateYear.toInt)),
                                               Some(DayMonthYear(expectStartCaringPermanentBreakDateDay.toInt,expectStartCaringPermanentBreakDateMonth.toInt,expectStartCaringPermanentBreakDateYear.toInt))))

           form.medicalCareDuringBreak must equalTo(yes)
           form.moreAboutChanges must equalTo(Some("I do not have changes"))
         }
       )
    }

    "check errors for mandatory fields" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartTime" -> breaksInCareStartTime,
          "moreAboutChanges" -> "I do not have changes"
        )
      ).fold(
        formWithErrors => {
            formWithErrors.errors.size must equalTo(5)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "Somewhere else text is mandatory when 'where was the person you care for' 'somewhere else' option is selected" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
          "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
          "breaksInCareStartDate.year" -> recentBreakFromCaringDateYear,
          "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.SomewhereElse,
          "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "breakEnded.answer" -> yes,
          "breakEnded.endDate.day" -> breakEndedEndDateDay,
          "breakEnded.endDate.month" -> breakEndedEndDateMonth,
          "breakEnded.endDate.year" -> breakEndedEndDateYear,
          "medicalCareDuringBreak" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.size must equalTo(1)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "Somewhere else text is mandatory when 'where were you' 'somewhere else' option is selected" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
          "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
          "breaksInCareStartDate.year" -> recentBreakFromCaringDateYear,
          "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.SomewhereElse,
          "breakEnded.answer" -> yes,
          "breakEnded.endDate.day" -> breakEndedEndDateDay,
          "breakEnded.endDate.month" -> breakEndedEndDateMonth,
          "breakEnded.endDate.year" -> breakEndedEndDateYear,
          "medicalCareDuringBreak" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.size must equalTo(1)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "'when did the break from caring end' is mandatory when 'Has this break from caring ended' is yes" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
          "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
          "breaksInCareStartDate.year" -> recentBreakFromCaringDateYear,
          "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "breakEnded.answer" -> yes,
          "medicalCareDuringBreak" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.size must equalTo(1)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "'Do you expect to start caring again' is mandatory when 'Has this break from caring ended' is no" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
          "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
          "breaksInCareStartDate.year" -> recentBreakFromCaringDateYear,
          "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "breakEnded.answer" -> no,
          "medicalCareDuringBreak" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.size must equalTo(1)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "'permanent break date' is mandatory when 'do you expect to start caring again' is no" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
          "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
          "breaksInCareStartDate.year" -> recentBreakFromCaringDateYear,
          "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "breakEnded.answer" -> no,
          "expectStartCaring.answer" -> no,
          "medicalCareDuringBreak" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.size must equalTo(1)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject invalid dates" in {
      G7BreaksInCare.form.bind(
        Map(
          "breaksInCareStartDate.day" -> recentBreakFromCaringDateDay,
          "breaksInCareStartDate.month" -> recentBreakFromCaringDateMonth,
          "breaksInCareStartDate.year" -> "wwww",
          "wherePersonBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "whereYouBreaksInCare.answer" -> CircsBreaksWhereabouts.Hospital,
          "breakEnded.answer" -> yes,
          "breakEnded.endDate.day" -> breakEndedEndDateDay,
          "breakEnded.endDate.month" -> breakEndedEndDateMonth,
          "breakEnded.endDate.year" -> "yyyy",
          "expectStartCaring.answer" -> yes,
          "expectStartCaring.expectStartCaringDate.day" -> expectStartCaringDateDay,
          "expectStartCaring.expectStartCaringDate.month" -> expectStartCaringDateMonth,
          "expectStartCaring.expectStartCaringDate.year" -> "000h",
          "expectStartCaring.permanentBreakDate.day" -> expectStartCaringPermanentBreakDateDay,
          "expectStartCaring.permanentBreakDate.month" -> expectStartCaringPermanentBreakDateMonth,
          "expectStartCaring.permanentBreakDate.year" -> "100b",
          "medicalCareDuringBreak" -> yes
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.number")
          formWithErrors.errors.size must equalTo(4)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }section("unit", models.domain.CircumstancesAddressChange.id)
}
