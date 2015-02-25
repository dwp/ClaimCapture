package monitoring

import org.specs2.mutable.Specification
import models.domain._
import org.specs2.mock.Mockito
import models.view.CachedClaim
import models.yesNo.{YesNoWithText, YesNoWithEmployerAndMoney, YesNoWithDate, YesNo}
import models.DayMonthYear
import models.domain.Claim
import scala.Some
import models.MultiLineAddress
import play.api.test.WithBrowser

class ClaimBotCheckingSpec extends Specification with Mockito with CachedClaim {

  val controller = new ClaimBotChecking {}

  var claim = copyInstance(new Claim()
    .update(Benefits(Benefits.pip))
    .update(Hours("no"))
    .update(LivesInGB("no"))
    .update(Over16("no")))


  private def createJob(jobId: String, questionGroup: QuestionGroup with controllers.Iteration.Identifier): Iteration = {
    val jobDetails = JobDetails(jobId)
    val job = Iteration(jobId).update(jobDetails).update(questionGroup)
    job
  }

  "Claim submission" should {
    "be flagged for completing sections too quickly e.g. a bot" in new WithBrowser {
      controller.checkTimeToCompleteAllSections(claim, currentTime = 0) should beTrue
    }

    "be completed slow enough to be human" in new WithBrowser {
      controller.checkTimeToCompleteAllSections(claim, currentTime = Long.MaxValue) should beFalse
    }

    "returns false given did not answer any honeyPot question groups" in {
      controller.honeyPot(Claim()) should beFalse
    }

    "returns false given MoreAboutTheCare answered yes and honeyPot filled" in {
      val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given MoreAboutTheCare answered no and honeyPot not filled" in {
      val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given MoreAboutTheCare answered no and honeyPot filled" in {
      val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = Some(DayMonthYear()))))
      controller.honeyPot(claim) should beTrue
    }

    /************* start employment pension and job expenses ********************/

    "returns true given PensionAndExpenses pension expenses honeyPot not filled (pension expenses entered)" in {
      val aboutExpenses = PensionAndExpenses(
        "12345",
        YesNoWithText("no",None),
        YesNoWithText("no",Some("Some expenses"))
       )

      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given PensionAndExpenses pension expenses honeyPot filled (pension expenses entered)" in {
      val aboutExpenses = PensionAndExpenses(
        "12345",
        YesNoWithText("no",None),
        YesNoWithText("yes",Some("Some expenses"))
      )

      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)
      controller.honeyPot(claim) should beFalse
    }

    "returns false given PensionAndExpenses pension expenses honeyPot filled (pension expenses entered) for more than one job" in {
      val aboutExpenses1 = PensionAndExpenses(
        "12345",
        YesNoWithText("no",None),
        YesNoWithText("yes",Some("Some expenses"))
      )

      val aboutExpenses2 = PensionAndExpenses(
        "12345",
        YesNoWithText("no",None),
        YesNoWithText("yes",Some("Some other expenses"))
      )

      val jobs = new Jobs().update(createJob("12345", aboutExpenses1)).update(createJob("123456", aboutExpenses1)).update(createJob("1234567", aboutExpenses2)).update(createJob("12345678", aboutExpenses1)).update(createJob("123456789", aboutExpenses2))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given PensionAndExpenses job expenses honeyPot not filled (job expenses entered)" in {
      val aboutExpenses = PensionAndExpenses(
        "12345",
        YesNoWithText("no",Some("Some expenses")),
        YesNoWithText("no")
      )
      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given PensionAndExpenses job expenses honeyPot filled (job expenses entered)" in {
      val aboutExpenses = PensionAndExpenses(
        "12345",
        YesNoWithText("yes",Some("Some expenses")),
        YesNoWithText("no")
      )
      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns false given PensionAndExpenses job expenses honeyPot filled (job expenses entered) for more than one job" in {
      val aboutExpenses1 = PensionAndExpenses(
        "",
        YesNoWithText("yes",Some("Some expenses")),
        YesNoWithText("no")
      )

      val aboutExpenses2 = PensionAndExpenses(
        "",
        YesNoWithText("yes",Some("Some other expenses")),
        YesNoWithText("no")
      )

      val jobs = new Jobs().update(createJob("12345", aboutExpenses1)).update(createJob("123456", aboutExpenses1)).update(createJob("1234567", aboutExpenses2)).update(createJob("12345678", aboutExpenses2)).update(createJob("123456789", aboutExpenses2))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    /************* end employment about expenses related *****************/

    "returns true given SelfEmploymentPensionAndExpenses pension expenses honeyPot not filled (pension expenses entered)" in {
      val aboutExpenses = SelfEmploymentPensionsAndExpenses(
        YesNoWithText("no",None),
        YesNoWithText("no",Some("Some expenses"))
      )

      val claim = Claim().update(aboutExpenses)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given SelfEmploymentPensionAndExpenses pension expenses honeyPot filled (pension expenses entered)" in {
      val aboutExpenses = SelfEmploymentPensionsAndExpenses(
        YesNoWithText("no",None),
        YesNoWithText("yes",Some("Some expenses"))
      )

      val claim = Claim().update(aboutExpenses)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given SelfEmploymentPensionsAndExpenses job expenses honeyPot not filled (job expenses entered)" in {
      val aboutExpenses = SelfEmploymentPensionsAndExpenses(
        YesNoWithText("no",Some("Some expenses")),
        YesNoWithText("no")
      )
      val claim = Claim().update(aboutExpenses)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given SelfEmploymentPensionsAndExpenses job expenses honeyPot filled (job expenses entered)" in {
      val aboutExpenses = SelfEmploymentPensionsAndExpenses(
        YesNoWithText("yes",Some("Some expenses")),
        YesNoWithText("no")
      )
      val claim = Claim().update(aboutExpenses)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given AboutOtherMoney answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), Some("Toys R Us"), Some("12"), howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))

      controller.honeyPot(claim) should beTrue
    }

    "returns false given StatutorySickPay answered yes and honeyPot filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("yes", howMuch = Some("12"), None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", howMuch = None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("yes",  None, Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly)), None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given StatutorySickPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, employersName = Some("some employersName"), None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given StatutorySickPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("yes", None, None, None, Some(MultiLineAddress(Some("some lineOne"))), None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given StatutorySickPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, Some("PR1A4JQ")),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given OtherStatutoryPay answered yes and honeyPot filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("yes", Some("12"), None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given OtherStatutoryPay answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", howMuch=Some("12"), None, None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly)), None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, employersName = Some("some employersName"), None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, Some(MultiLineAddress(Some("some lineOne"))), None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, Some("PR1A4JQ"))))
      controller.honeyPot(claim) should beTrue
    }
  }
}
