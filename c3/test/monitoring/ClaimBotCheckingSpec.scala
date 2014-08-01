package monitoring

import org.specs2.mutable.Specification
import models.domain._
import org.specs2.mock.Mockito
import models.view.CachedClaim
import models.yesNo.{YesNoWithEmployerAndMoney, YesNoWithDate, YesNo}
import models.DayMonthYear
import models.domain.Claim
import scala.Some
import models.MultiLineAddress
import play.api.test.WithBrowser

class ClaimBotCheckingSpec extends Specification with Mockito with CachedClaim {

  val controller = new ClaimBotChecking {}

  var claim = copyInstance(new Claim()
    .update(Benefits("no"))
    .update(Hours("no"))
    .update(LivesInGB("no"))
    .update(Over16("no")))


  private def createJob(jobId: String, questionGroup: QuestionGroup with Job.Identifier): Job = {
    val jobDetails = JobDetails(jobId)
    val job = Job(jobId).update(jobDetails).update(questionGroup)
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

    "returns false given NormalResidenceAndCurrentLocation honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(AboutExpenses(howOftenLookAfterChildren = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))
      controller.honeyPot(claim) should beFalse
    }

    /************* start childcare ********************/

    "returns false given AboutExpense childcare expenses honeyPot not filled (frequency other and text entered)" in {
      val aboutExpenses = AboutExpenses(
        haveExpensesForJob = "no",
        payAnyoneToLookAfterChildren = "yes",
        payAnyoneToLookAfterPerson = "no",
        nameLookAfterChildren = Some("Jane Doe"),
        howMuchLookAfterChildren = Some("125.40"),
        relationToYouLookAfterChildren = Some("none"),
        relationToPersonLookAfterChildren = Some("some relation"),
        howOftenLookAfterChildren = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other,
        other = Some("other text"))))

      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given AboutExpense childcare expenses honeyPot filled (frequency not other and text entered)" in {
      val aboutExpenses = AboutExpenses(
        haveExpensesForJob = "no",
        payAnyoneToLookAfterChildren = "yes",
        payAnyoneToLookAfterPerson = "no",
        nameLookAfterChildren = Some("Jane Doe"),
        howMuchLookAfterChildren = Some("125.40"),
        relationToYouLookAfterChildren = Some("none"),
        relationToPersonLookAfterChildren = Some("some relation"),
        howOftenLookAfterChildren = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Monthly,
          other = Some("other text"))))

      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)
      controller.honeyPot(claim) should beTrue
    }

    "returns true given AboutExpenses childcare expenses honeyPot filled (frequency not other and text entered) for more than one job" in {
      val childCareExpense =
        AboutExpenses(
          haveExpensesForJob = "no",
          payAnyoneToLookAfterChildren = "yes",
          payAnyoneToLookAfterPerson = "no",
          nameLookAfterChildren = Some("Jane Doe"),
          howMuchLookAfterChildren = Some("125.40"),
          relationToYouLookAfterChildren = Some("none"),
          relationToPersonLookAfterChildren = Some("some relation"),
          howOftenLookAfterChildren = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other,
            other = Some("other text"))))

      val childCareExpenseWithHoneyPot = AboutExpenses(
        haveExpensesForJob = "no",
        payAnyoneToLookAfterChildren = "yes",
        payAnyoneToLookAfterPerson = "no",
        nameLookAfterChildren = Some("Jane Doe"),
        howMuchLookAfterChildren = Some("125.40"),
        relationToYouLookAfterChildren = Some("none"),
        relationToPersonLookAfterChildren = Some("some relation"),
        howOftenLookAfterChildren = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Monthly,
          other = Some("other text"))))

      val jobs = new Jobs().update(createJob("12345", childCareExpense)).update(createJob("123456", childCareExpense)).update(createJob("1234567", childCareExpenseWithHoneyPot)).update(createJob("12345678", childCareExpense)).update(createJob("123456789", childCareExpenseWithHoneyPot))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given AboutExpenses personYouCareFor expenses honeyPot not filled (frequency not other)" in {
      val aboutExpenses = AboutExpenses(
        haveExpensesForJob = "no",
        payAnyoneToLookAfterChildren = "no",
        payAnyoneToLookAfterPerson = "yes",
        nameLookAfterPerson= Some("Jane Doe"),
        howMuchLookAfterPerson = Some("125.40"),
        relationToYouLookAfterPerson = Some("none"),
        relationToPersonLookAfterPerson = Some("some relation"),
        howOftenLookAfterPerson = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Monthly,
          other = None)))
      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns false given AboutExpenses personYouCareFor expenses honeyPot not filled (frequency other and text entered)" in {
      val aboutExpenses =
        AboutExpenses(
          haveExpensesForJob = "no",
          payAnyoneToLookAfterChildren = "no",
          payAnyoneToLookAfterPerson = "yes",
          nameLookAfterPerson = Some("Jane Doe"),
          howMuchLookAfterPerson = Some("125.40"),
          relationToYouLookAfterPerson = Some("none"),
          relationToPersonLookAfterPerson = Some("some relation"),
          howOftenLookAfterPerson = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other,
            other = Some("other text"))))
      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given AboutExpenses personYouCareFor expenses honeyPot filled (frequency not other and text entered)" in {
      val aboutExpenses =
        AboutExpenses(
          haveExpensesForJob = "no",
          payAnyoneToLookAfterChildren = "no",
          payAnyoneToLookAfterPerson = "yes",
          nameLookAfterPerson = Some("Jane Doe"),
          howMuchLookAfterPerson = Some("125.40"),
          relationToYouLookAfterPerson = Some("none"),
          relationToPersonLookAfterPerson = Some("some relation"),
          howOftenLookAfterPerson = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly,
            other = Some("other text"))))
      val jobs = new Jobs().update(createJob("12345", aboutExpenses))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns true given AboutExpenses personYouCareFor expenses honeyPot filled (frequency not other and text entered) for more than one job" in {
      val personYouCareForExpenses =   AboutExpenses(
        haveExpensesForJob = "no",
        payAnyoneToLookAfterChildren = "no",
        payAnyoneToLookAfterPerson = "yes",
        nameLookAfterPerson = Some("Jane Doe"),
        howMuchLookAfterPerson = Some("125.40"),
        relationToYouLookAfterPerson = Some("none"),
        relationToPersonLookAfterPerson = Some("some relation"),
        howOftenLookAfterPerson = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other,
          other = Some("other text"))))
      val personYouCareForExpensesWithHoneyPot =  AboutExpenses(
        haveExpensesForJob = "no",
        payAnyoneToLookAfterChildren = "no",
        payAnyoneToLookAfterPerson = "yes",
        nameLookAfterPerson = Some("Jane Doe"),
        howMuchLookAfterPerson = Some("125.40"),
        relationToYouLookAfterPerson = Some("none"),
        relationToPersonLookAfterPerson = Some("some relation"),
        howOftenLookAfterPerson = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly,
          other = Some("other text"))))

      val jobs = new Jobs().update(createJob("12345", personYouCareForExpenses)).update(createJob("123456", personYouCareForExpenses)).update(createJob("1234567", personYouCareForExpensesWithHoneyPot)).update(createJob("12345678", personYouCareForExpenses)).update(createJob("123456789", personYouCareForExpensesWithHoneyPot))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    /************* end about expenses related *****************/

    "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given ChildcareExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given ExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given ExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given ExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given PensionSchemes answered no and honeyPot not filled (frequency not other)" in {
      val pensionScheme = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None)
      val jobs = new Jobs().update(createJob("12345", pensionScheme))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse

    }

    "returns false given PensionSchemes answered yes and honeyPot filled (frequency not other)" in {
      val pensionScheme = PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      val jobs = new Jobs().update(createJob("12345", pensionScheme))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given PensionSchemes answered no and honeyPot filled" in {
      val pensionScheme = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      val jobs = new Jobs().update(createJob("12345", pensionScheme))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns true given PensionSchemes honeyPot filled (frequency not other and text entered)" in {
      val pensionScheme = PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      val jobs = new Jobs().update(createJob("12345", pensionScheme))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given PensionSchemes answered no and honey pot not filled for more than one job" in {
      val pensionScheme = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None)
      val jobs = new Jobs().update(createJob("12345", pensionScheme)).update(createJob("123456", pensionScheme)).update(createJob("1234567", pensionScheme)).update(createJob("12345678", pensionScheme)).update(createJob("123456789", pensionScheme))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given PensionSchemes answered no and honey pot filled for more than one job" in {
      val pensionScheme = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None)
      val pensionSchemeWithHoneyPot = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      val jobs = new Jobs().update(createJob("12345", pensionScheme)).update(createJob("123456", pensionScheme)).update(createJob("1234567", pensionSchemeWithHoneyPot)).update(createJob("12345678", pensionScheme)).update(createJob("123456789", pensionSchemeWithHoneyPot))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }


    "returns false given AboutOtherMoney answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("yes"), None, None, None,
        YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given AboutOtherMoney answered no and honeyPot howOften filled" in {

      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), Some("Toys R Us"), Some("12"), howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))

      controller.honeyPot(claim) should beTrue
    }

    "returns false given StatutorySickPay answered yes and honeyPot filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("yes", howMuch = Some("12"), None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", howMuch = None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("yes",  None, Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly)), None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given StatutorySickPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, employersName = Some("some employersName"), None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given StatutorySickPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("yes", None, None, None, Some(MultiLineAddress(Some("some lineOne"))), None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given StatutorySickPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, Some("PR1A4JQ")),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given OtherStatutoryPay answered yes and honeyPot filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("yes", Some("12"), None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given OtherStatutoryPay answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", howMuch=Some("12"), None, None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly)), None, None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, employersName = Some("some employersName"), None, None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, Some(MultiLineAddress(Some("some lineOne"))), None)))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(AboutOtherMoney(YesNo("no"), YesNo("no"), None, None, None, YesNoWithEmployerAndMoney("no", None, None, None, None, None),
        YesNoWithEmployerAndMoney("no", None, None, None, None, Some("PR1A4JQ"))))
      controller.honeyPot(claim) should beTrue
    }


  }
}
