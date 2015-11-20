package controllers.circs.s2_report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class G9EmploymentChangeFormSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val dontknow = "dontknow"
  val stillCaringDateDay = 10
  val stillCaringDateMonth = 11
  val stillCaringDateYear = 2012
  val startDateDay = 1
  val startDateMonth = 12
  val startDateYear = 2012
  val selfEmployed = "self-employed"
  val employed = "employed"
  val selfEmployedTypeOfWork = "IT Consultant"

  "Report a change in your circumstances - Employment Form" should {
    "map not caring and not started yet self-employment data into case class" in new WithApplication {
      G9EmploymentChange.form.bind(
        Map(
          "stillCaring.answer" -> no,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring.date.year" -> stillCaringDateYear.toString,
          "hasWorkStartedYet.answer" -> no,
          "hasWorkStartedYet.dateWhenWillItStart.day" -> startDateDay.toString,
          "hasWorkStartedYet.dateWhenWillItStart.month" -> startDateMonth.toString,
          "hasWorkStartedYet.dateWhenWillItStart.year" -> startDateYear.toString,
          "typeOfWork.answer" -> selfEmployed,
          "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
          "typeOfWork.selfEmployedTotalIncome" -> no
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.stillCaring.answer must equalTo(no)
            f.stillCaring.date.get must equalTo(DayMonthYear(Some(stillCaringDateDay), Some(stillCaringDateMonth), Some(stillCaringDateYear), None, None))
            f.hasWorkStartedYet.answer must equalTo(no)
            f.hasWorkStartedYet.date2.get must equalTo(DayMonthYear(Some(startDateDay), Some(startDateMonth), Some(startDateYear), None, None))
            f.typeOfWork.answer must equalTo(selfEmployed)
            f.typeOfWork.text2a.get must equalTo(selfEmployedTypeOfWork)
            f.typeOfWork.answer2.get must equalTo(no)
          }
        )
    }

    "map caring and not started yet self-employment data into case class" in new WithApplication {
      G9EmploymentChange.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "hasWorkStartedYet.answer" -> no,
          "hasWorkStartedYet.dateWhenWillItStart.day" -> startDateDay.toString,
          "hasWorkStartedYet.dateWhenWillItStart.month" -> startDateMonth.toString,
          "hasWorkStartedYet.dateWhenWillItStart.year" -> startDateYear.toString,
          "typeOfWork.answer" -> selfEmployed,
          "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
          "typeOfWork.selfEmployedTotalIncome" -> yes
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.stillCaring.answer must equalTo(yes)
            f.hasWorkStartedYet.answer must equalTo(no)
            f.hasWorkStartedYet.date2.get must equalTo(DayMonthYear(Some(startDateDay), Some(startDateMonth), Some(startDateYear), None, None))
            f.typeOfWork.answer must equalTo(selfEmployed)
            f.typeOfWork.text2a.get must equalTo(selfEmployedTypeOfWork)
            f.typeOfWork.answer2.get must equalTo(yes)
          }
        )
    }

    "map caring and ongoing started self-employment data into case class" in new WithApplication {
      G9EmploymentChange.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "hasWorkStartedYet.answer" -> yes,
          "hasWorkStartedYet.dateWhenStarted.day" -> startDateDay.toString,
          "hasWorkStartedYet.dateWhenStarted.month" -> startDateMonth.toString,
          "hasWorkStartedYet.dateWhenStarted.year" -> startDateYear.toString,
          "hasWorkFinishedYet.answer" -> no,
          "typeOfWork.answer" -> selfEmployed,
          "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
          "typeOfWork.selfEmployedTotalIncome" -> dontknow
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.stillCaring.answer must equalTo(yes)
            f.hasWorkStartedYet.answer must equalTo(yes)
            f.hasWorkStartedYet.date1.get must equalTo(DayMonthYear(Some(startDateDay), Some(startDateMonth), Some(startDateYear), None, None))
            f.hasWorkFinishedYet.answer.get must equalTo(no)
            f.typeOfWork.answer must equalTo(selfEmployed)
            f.typeOfWork.text2a.get must equalTo(selfEmployedTypeOfWork)
            f.typeOfWork.answer2.get must equalTo(dontknow)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
