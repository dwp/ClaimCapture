package controllers.circs.employment_change

import app.ConfigProperties._
import controllers.circs.report_changes.GEmploymentChange
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.{WithBrowser, WithApplication}
import utils.pageobjects.circumstances.report_changes.GEmploymentChangePage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithApplication, WithBrowser}

class GEmploymentChangeSpec extends Specification {
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
  val selfEmploymentChangePath = "DWPCAChangeOfCircumstances//EmploymentChange//SelfEmployment//MoreAboutChanges//Answer"
  val nextPageUrl = GCircsDeclarationPage.url

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
    "typeOfWork.selfEmployedTotalIncome" -> no,
    "paidMoneyYet.answer" -> no
  )

  val validCaringSelfEmploymentNotYetStartedFormInput = Seq(
    "stillCaring.answer" -> yes,
    "hasWorkStartedYet.answer" -> no,
    "hasWorkStartedYet.dateWhenWillItStart.day" -> startDateDay.toString,
    "hasWorkStartedYet.dateWhenWillItStart.month" -> startDateMonth.toString,
    "hasWorkStartedYet.dateWhenWillItStart.year" -> startDateYear.toString,
    "typeOfWork.answer" -> selfEmployed,
    "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
    "typeOfWork.selfEmployedTotalIncome" -> yes,
    "paidMoneyYet.answer" -> no
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
    "typeOfWork.selfEmployedTotalIncome" -> dontknow,
    "paidMoneyYet.answer" -> no
  )

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances - Employment - Controller" should {
   "present 'CoC Employment Changes'" in new WithApplication with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

     val result = GEmploymentChange.present(request)
     status(result) mustEqual OK
   }

   "redirect to the next page after a valid not caring and self-employment not yet started details submission" in new WithApplication with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
       .withFormUrlEncodedBody(validNotCaringSelfEmploymentNotYetStartedFormInput: _*)

     val result = GEmploymentChange.submit(request)
     val bodyText: String = contentAsString(result)
     println(bodyText)
     redirectLocation(result) must beSome(nextPageUrl)
   }

   "redirect to the next page after a valid caring and self-employment not yet started details submission" in new WithApplication with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
       .withFormUrlEncodedBody(validCaringSelfEmploymentNotYetStartedFormInput: _*)

     val result = GEmploymentChange.submit(request)
     redirectLocation(result) must beSome(nextPageUrl)
   }

   "redirect to the next page after a valid caring and ongoing self-employment started details submission" in new WithApplication with MockForm {
     val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
       .withFormUrlEncodedBody(validCaringAndOngoingSelfEmploymentStartedFormInput: _*)

     val result = GEmploymentChange.submit(request)
     redirectLocation(result) must beSome(nextPageUrl)
   }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, selfEmploymentChangePath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getStringProperty("xml.schema.version")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, selfEmploymentChangePath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GEmploymentChangePage.url)
      val anythingElse = browser.$("#typeOfWork_selfEmployedMoreAboutChanges")
      val countdown = browser.$("#typeOfWork_selfEmployedMoreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
 }
 section("unit", models.domain.CircumstancesReportChanges.id)
}
