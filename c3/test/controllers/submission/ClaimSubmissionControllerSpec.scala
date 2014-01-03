package controllers.submission

import org.specs2.mutable.Specification
import models.domain._
import models._
import models.yesNo._
import org.specs2.mock.Mockito
import play.api.cache.Cache
import play.api.test.{FakeRequest, WithApplication}
import jmx.JMXActors
import java.util.concurrent.TimeUnit
import models.MultiLineAddress
import models.domain.Claim
import models.yesNo.YesNo
import models.view.CachedClaim
import play.api.mvc.{SimpleResult, AnyContent, Request}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import jmx.inspectors.{ClaimStatistics, GetClaimStatistics}

class ClaimSubmissionControllerSpec extends Specification with Mockito with CachedClaim {
  val controller = new ClaimSubmissionController(new Submitter {
    def submit(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = Future(mock[SimpleResult])
  })

  var claim = copyInstance(new Claim()
    .update(Benefits("no"))
    .update(Hours("no"))
    .update(LivesInGB("no"))
    .update(Over16("no")))


  private def createJob(jobId: String, questionGroup: QuestionGroup with Job.Identifier): Job = {
    var jobDetails = JobDetails(jobId)
    var job = Job(jobId).update(jobDetails).update(questionGroup)
    job
  }


  "Claim submission" should {
    "fire 'claim submitted' message upon claim submission" in new WithApplication with Claiming {

      import scala.concurrent.Await
      import akka.pattern.ask
      import akka.util.Timeout
      import JMXActors.claimInspector

      implicit val timeout = Timeout(10, TimeUnit.SECONDS)

      Cache.set(claimKey, claim)
      implicit val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      controller.submit(request)

      val future = claimInspector ? GetClaimStatistics

      val claimStatistics = Await.result(future, timeout.duration).asInstanceOf[ClaimStatistics]

      claimStatistics should beLike {
        case ClaimStatistics(numberOfClaims, averageTime) => numberOfClaims should be_>(0)
      }
    }

    "be flagged for completing sections too quickly e.g. a bot" in {
      controller.checkTimeToCompleteAllSections(claim, currentTime = 0) should beTrue
    }

    "be completed slow enough to be human" in {
      controller.checkTimeToCompleteAllSections(claim, currentTime = Long.MaxValue) should beFalse
    }

    "returns false given did not answer any honeyPot question groups" in {
      controller.honeyPot(Claim()) should beFalse
    }

    "returns false given TimeOutsideUK answered yes and honeyPot filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "yes", date = Some(DayMonthYear()), text = Some("some text"), goBack = Some(YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given TimeOutsideUK answered no and honeyPot not filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", date = None, text = None, goBack = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given TimeOutsideUK answered no and honeyPot date filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", date = Some(DayMonthYear()))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given TimeOutsideUK answered no and honeyPot text filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", text = Some("some text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given TimeOutsideUK answered no and honeyPot goBack filled" in {
      val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", goBack = Some(YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))))
      controller.honeyPot(claim) should beTrue
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

    "returns false given NormalResidenceAndCurrentLocation answered no and honeyPot filled" in {
      val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "no", text = Some("some text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given NormalResidenceAndCurrentLocation answered yes and honeyPot not filled" in {
      val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "yes", text = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given NormalResidenceAndCurrentLocation answered yes and honeyPot filled" in {
      val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "yes", text = Some("some text"))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given NormalResidenceAndCurrentLocation honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given ChildcareExpenses honeyPot not filled (frequency other and text entered)" in {
      val jobs = new Jobs().update(createJob("12345", ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text")))))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given ChildcareExpenses honeyPot filled (frequency not other and text entered)" in {
      val jobs = new Jobs().update(createJob("12345", ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns true given ChildcareExpenses honeyPot filled (frequency not other and text entered) for more than one job" in {

      val childCareExpense = ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text")))
      val childCareExpenseWithHoneyPot = ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))

      val jobs = new Jobs().update(createJob("12345", childCareExpense)).update(createJob("123456", childCareExpense)).update(createJob("1234567", childCareExpenseWithHoneyPot)).update(createJob("12345678", childCareExpense)).update(createJob("123456789", childCareExpenseWithHoneyPot))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }

    "returns false given PersonYouCareForExpenses honeyPot not filled (frequency not other)" in {
      val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given PersonYouCareForExpenses honeyPot not filled (frequency other and text entered)" in {
      val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given PersonYouCareForExpenses honeyPot filled (frequency not other and text entered)" in {
      val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))
      controller.honeyPot(claim) should beTrue
    }

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
      var jobs = new Jobs().update(createJob("12345", pensionScheme)).update(createJob("123456", pensionScheme)).update(createJob("1234567", pensionScheme)).update(createJob("12345678", pensionScheme)).update(createJob("123456789", pensionScheme))
      val claim = Claim().update(jobs)

      controller.honeyPot(claim) should beFalse
    }

    "returns true given PensionSchemes answered no and honey pot filled for more than one job" in {
      var pensionScheme = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None)
      var pensionSchemeWithHoneyPot = PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))
      var jobs = new Jobs().update(createJob("12345", pensionScheme)).update(createJob("123456", pensionScheme)).update(createJob("1234567", pensionSchemeWithHoneyPot)).update(createJob("12345678", pensionScheme)).update(createJob("123456789", pensionSchemeWithHoneyPot))
      var claim = Claim().update(jobs)

      controller.honeyPot(claim) should beTrue
    }


    "returns false given AboutOtherMoney answered yes and honeyPot filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("yes"), whoPaysYou = Some("some whoPaysYou")))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given AboutOtherMoney answered no and honeyPot not filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), whoPaysYou = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given AboutOtherMoney answered no and honeyPot whoPaysYou filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), whoPaysYou = Some("some whoPaysYou")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given AboutOtherMoney answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given AboutOtherMoney answered no and honeyPot howOften filled" in {
      val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given StatutorySickPay answered yes and honeyPot filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "yes", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given StatutorySickPay answered no and honeyPot not filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howMuch = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given StatutorySickPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersName = Some("some employersName")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersAddress = Some(MultiLineAddress(Some("some lineOne")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given StatutorySickPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersPostcode = Some("some employersPostcode")))
      controller.honeyPot(claim) should beTrue
    }

    "returns false given OtherStatutoryPay answered yes and honeyPot filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "yes", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beFalse
    }

    "returns false given OtherStatutoryPay answered no and honeyPot not filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howMuch = None))
      controller.honeyPot(claim) should beFalse
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howMuch filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howMuch = Some("some howMuch")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot howOften filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersName filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersName = Some("some employersName")))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersAddress filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersAddress = Some(MultiLineAddress(Some("some lineOne")))))
      controller.honeyPot(claim) should beTrue
    }

    "returns true given OtherStatutoryPay answered no and honeyPot employersPostcode filled" in {
      val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersPostcode = Some("some employersPostcode")))
      controller.honeyPot(claim) should beTrue
    }
  }
}