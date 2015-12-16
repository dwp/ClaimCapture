package xml

import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._
import models.DayMonthYear
import org.joda.time.DateTime
import models.PaymentFrequency
import models.domain.Claim
import utils.WithApplication
import models.yesNo.{YesNoWith1MandatoryFieldOnYes, YesNoWith2MandatoryFieldsOnYes, YesNoWithText}
import xml.claim.AssistedDecision

class AssistedDecisionSpec extends Specification {

  val whenGetPaid = "Monday"
  val employerOwesYouMoney = Some("no")

  section("unit")
  "Assisted section" should {
    "Create an assisted decision section if care less than 35 hours" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Do not spend 35 hours or more each week caring.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if care more than 35 hours" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if employment with £100.01 a week and no expenses, pension schemes." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job = Iteration("12", List(lastWage))
      val jobs = Jobs(List(job))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with £200.02 a fortnight and no expenses, pension schemes." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job = Iteration("12", List(lastWage))
      val jobs = Jobs(List(job))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with £400.04 every 4 weeks and no expenses, pension schemes." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job = Iteration("12", List(lastWage))
      val jobs = Jobs(List(job))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with £433.38 a month and no expenses, pension schemes." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job = Iteration("12", List(lastWage))
      val jobs = Jobs(List(job))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and no expenses, pension schemes." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))

      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job2 = Iteration("13", List(lastWage2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with 2 jobs one £100.01 a week and one 200.04 with no frequency." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))

      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job2 = Iteration("13", List(lastWage2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    } .pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and job 2 has an expense." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))

      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val expense2 = PensionAndExpenses(iterationID = "13", YesNoWithText("yes", Some("some pension expenses")), YesNoWithText("yes", Some("some pay for things")), YesNoWithText("yes", Some("some job expenses")))
      val job2 = Iteration("13", List(lastWage2, expense2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Not create an assisted decision section if employment with 2 jobs one £50 a week and one 100.04 four-weekly." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))
      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job2 = Iteration("13", List(lastWage2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if employment with 2 jobs one £100.01 a week and one 22.13 with not same amount each time." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))

      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job2 = Iteration("13", List(lastWage2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if employment with 2 jobs one £100.01 a week and one 22.13 both with not same amount each time." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))

      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job2 = Iteration("13", List(lastWage2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section only for caring hours if care less than 35 hours and employment with 2 jobs one £100.01 a week and one 200.04." in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(iterationID = "12",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job1 = Iteration("12", List(lastWage1))
      val lastWage2 = LastWage(iterationID = "13",
        oftenGetPaid = PaymentFrequency("Weekly"),
        whenGetPaid = whenGetPaid,
        lastPaidDate = DayMonthYear(),
        grossPay = "100.01",
        employerOwesYouMoney = employerOwesYouMoney)
      val job2 = Iteration("13", List(lastWage2))

      val jobs = Jobs(List(job1, job2))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must not contain "Total weekly gross pay"
      (xml \\ "Reason").text must contain("Do not spend 35 hours or more each week caring.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if less than 16 years old" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = YourDetails(dateOfBirth = DayMonthYear(now.minusYears(16).plusDays(1)))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Customer Date of Birth")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if care 16 years old" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = YourDetails(dateOfBirth = DayMonthYear(now.minusYears(16).minusDays(1)))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if date of claim > 3 months and 1 day" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3).plusDays(2)))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Date of Claim too far in the future. Potential disallowance.")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if date of claim <= 3 month and 1 day" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3)))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Date of Claim too far in the future. Potential disallowance."
    }

    "Create an assisted decision section if EEA pension" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = "yes", field1=Some(YesNoWith1MandatoryFieldOnYes(answer="yes"))))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Claimant or partner dependent on EEA pensions or benefits.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Exportability team.")
    }

    "Not create an assisted decision section if no EEA pension" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = "yes", field1=Some(YesNoWith1MandatoryFieldOnYes(answer="no"))))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Not create an assisted decision section if no EEA benefits claimed for" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = "yes", field2=Some(YesNoWith1MandatoryFieldOnYes(answer="no"))))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if EEA insurance or working" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
            val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3).plusDays(2)))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Date of Claim too far in the future.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance.")
    }.pendingUntilFixed("Postponed by business")

    "Not create an assisted decision section if no EEA insurance or working" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(nationality = "British", actualnationality=None, resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3)))
      val claim = Claim(CachedClaim.key).update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if person is not British" in new WithApplication {
      val residency = NationalityAndResidency(nationality = "Another nationality", actualnationality=Some("French"), resideInUK = YesNoWithText("yes", None))
      val claim = Claim(CachedClaim.key).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Person is not British.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Exportability team.")
    }

    "Create an assisted decision section if person does not normally live in England, Scotland or Wales" in new WithApplication {
      val residency = NationalityAndResidency(nationality = "British", resideInUK=YesNoWithText("no", Some("France")))
      val claim = Claim(CachedClaim.key).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Person does not normally live in England, Scotland or Wales.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Exportability team.")
    }

    "Not create an assisted decision section if person normally lives in England, Scotland or Wales" in new WithApplication {
      val residency = NationalityAndResidency(nationality = "British", resideInUK = YesNoWithText("yes", None))
      val claim = Claim(CachedClaim.key).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

  }
  section("unit")
}
