package controllers.circs.s2_report_changes

import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import utils.{WithApplication, LightFakeApplication}

class G9EmploymentChangeSpec extends Specification {
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

  val validNotCaringSelfEmploymentNotYetStartedFormInput = Seq(
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

  val validCaringSelfEmploymentNotYetStartedFormInput = Seq(
    "stillCaring.answer" -> yes,
    "hasWorkStartedYet.answer" -> no,
    "hasWorkStartedYet.dateWhenWillItStart.day" -> startDateDay.toString,
    "hasWorkStartedYet.dateWhenWillItStart.month" -> startDateMonth.toString,
    "hasWorkStartedYet.dateWhenWillItStart.year" -> startDateYear.toString,
    "typeOfWork.answer" -> selfEmployed,
    "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
    "typeOfWork.selfEmployedTotalIncome" -> yes
  )

  val validCaringAndOngoingSelfEmploymentStartedFormInput = Seq(
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

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances - Employment - Controller" should {
   "present 'CoC Employment Changes'" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

     val result = G9EmploymentChange.present(request)
     status(result) mustEqual OK
   }

   "redirect to the next page after a valid not caring and self-employment not yet started details submission" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
       .withFormUrlEncodedBody(validNotCaringSelfEmploymentNotYetStartedFormInput: _*)

     val result = G9EmploymentChange.submit(request)
     redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
   }

   "redirect to the next page after a valid caring and self-employment not yet started details submission" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
       .withFormUrlEncodedBody(validCaringSelfEmploymentNotYetStartedFormInput: _*)

     val result = G9EmploymentChange.submit(request)
     redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
   }

   "redirect to the next page after a valid caring and ongoing self-employment started details submission" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
       .withFormUrlEncodedBody(validCaringAndOngoingSelfEmploymentStartedFormInput: _*)

     val result = G9EmploymentChange.submit(request)
     redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
   }
 }
 section("unit", models.domain.CircumstancesReportChanges.id)
}
