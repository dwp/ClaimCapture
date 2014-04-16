package xml

import models.domain._
import org.specs2.mutable.{Tags, Specification}
import app.StatutoryPaymentFrequency
import models.DayMonthYear
import org.joda.time.DateTime
import models.PaymentFrequency
import models.domain.Claim
import scala.Some
import models.yesNo.YesNoWithText
import xml.claim.AssistedDecision


class AssistedDecisionSpec extends Specification with Tags {

  "Assisted section" should {

    "Create an assisted decision section if care less than 35 hours" in {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val claim = Claim().update(moreAboutTheCare).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Do not spend 35 hours or more each week caring.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if care more than 35 hours" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val claim = Claim().update(moreAboutTheCare).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if employment with £100.01 a week and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(jobID = "12", grossPay = "100.01", sameAmountEachTime = Some("yes"))
      val additionalWage = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job = Job("12", List(lastWage, additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with £200.02 a fortnight and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(jobID = "12", grossPay = "200.02", sameAmountEachTime = Some("yes"))
      val additionalWage = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Fortnightly))
      val job = Job("12", List(lastWage, additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with £400.04 every 4 weeks and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(jobID = "12", grossPay = "400.04", sameAmountEachTime = Some("yes"))
      val additionalWage = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.FourWeekly))
      val job = Job("12", List(lastWage, additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with £433.38 a month and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage = LastWage(jobID = "12", grossPay = "433.38", sameAmountEachTime = Some("yes"))
      val additionalWage = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Monthly))
      val job = Job("12", List(lastWage, additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "50", sameAmountEachTime = Some("yes"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04", sameAmountEachTime = Some("yes"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.FourWeekly))
      val job2 = Job("13", List(lastWage2, additionalWage2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if employment with 2 jobs one £100.01 a week and one 200.04 with no frequency." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "100.01", sameAmountEachTime = Some("yes"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04", sameAmountEachTime = Some("yes"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13")
      val job2 = Job("13", List(lastWage2, additionalWage2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    } .pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and job 2 has an expense." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "50", sameAmountEachTime = Some("yes"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04", sameAmountEachTime = Some("yes"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.FourWeekly))
      val expense2 = ChildcareExpenses(jobID = "13", howMuchCostChildcare = "2")
      val job2 = Job("13", List(lastWage2, additionalWage2, expense2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Not create an assisted decision section if employment with 2 jobs one £50 a week and one 100.04 four-weekly." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "50", sameAmountEachTime = Some("yes"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "100.04", sameAmountEachTime = Some("yes"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.FourWeekly))
      val job2 = Job("13", List(lastWage2, additionalWage2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if employment with 2 jobs one £100.01 a week and one 22.13 with not same amount each time." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "100.01", sameAmountEachTime = Some("yes"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "22.13", sameAmountEachTime = Some("no"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13")
      val job2 = Job("13", List(lastWage2, additionalWage2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Total weekly gross pay 100.01 > £100.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance, but need to check advisory additional notes.")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if employment with 2 jobs one £100.01 a week and one 22.13 both with not same amount each time." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "100.01", sameAmountEachTime = Some("no"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "22.13", sameAmountEachTime = Some("no"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13")
      val job2 = Job("13", List(lastWage2, additionalWage2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section only for caring hours if care less than 35 hours and employment with 2 jobs one £100.01 a week and one 200.04." in {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val lastWage1 = LastWage(jobID = "12", grossPay = "100.01", sameAmountEachTime = Some("yes"))
      val additionalWage1 = AdditionalWageDetails(jobID = "12", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job1 = Job("12", List(lastWage1, additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04", sameAmountEachTime = Some("yes"))
      val additionalWage2 = AdditionalWageDetails(jobID = "13", oftenGetPaid = PaymentFrequency(StatutoryPaymentFrequency.Weekly))
      val job2 = Job("13", List(lastWage2, additionalWage2))
      val jobs = Jobs(List(job1, job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must not contain "Total weekly gross pay"
      (xml \\ "Reason").text must contain("Do not spend 35 hours or more each week caring.")
    }.pendingUntilFixed("Postponed by busines")

    "Create an assisted decision section if less than 16 years old" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = YourDetails(dateOfBirth = DayMonthYear(now.minusYears(16).plusDays(1)))
      val claim = Claim().update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Customer Date of Birth")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if care 16 years old" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = YourDetails(dateOfBirth = DayMonthYear(now.minusYears(16).minusDays(1)))
      val claim = Claim().update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if AFIP is yes" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val moreAboutThePerson = TheirPersonalDetails(armedForcesPayment = "yes")
      val claim = Claim().update(moreAboutTheCare).update(moreAboutThePerson).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Person receives Armed Forces Independence Payment.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Armed Forces Independent Payments team.")
    }

    "Not create an assisted decision section if AFIP is no" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val moreAboutThePerson = TheirPersonalDetails(armedForcesPayment = "no")
      val claim = Claim().update(moreAboutTheCare).update(moreAboutThePerson).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if date of claim > 3 months and 1 day" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3).plusDays(2)))
      val claim = Claim().update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Date of Claim too far in the future. Potential disallowance.")
    }.pendingUntilFixed("Postponed by busines")

    "Not create an assisted decision section if date of claim <= 3 month and 1 day" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3)))
      val claim = Claim().update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Date of Claim too far in the future. Potential disallowance."
    }

    "Create an assisted decision section if EEA pension" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(benefitsFromEEA = "yes")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Claimant or partner dependent on EEA pensions or benefits.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Exportability team.")
    }

    "Not create an assisted decision section if no EEA pension" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(benefitsFromEEA = "no")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if EEA benefits claimed for" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(claimedForBenefitsFromEEA = "yes")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Claimant or partner dependent on EEA pensions or benefits.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Exportability team.")
    }

    "Not create an assisted decision section if no EEA benefits claimed for" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(workingForEEA = "no")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if EEA insurance or working" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3).plusDays(2)))
      val claim = Claim().update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Date of Claim too far in the future.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance.")
    }.pendingUntilFixed("Postponed by business")

    "Not create an assisted decision section if no EEA insurance or working" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes", None))
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3)))
      val claim = Claim().update(moreAboutTheCare).update(details).update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

    "Create an assisted decision section if person does not normally live in England, Scotland or Wales" in {
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("no"))
      val claim = Claim().update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Person does not normally live in England, Scotland or Wales.")
      (xml \\ "RecommendedDecision").text must contain ("Transfer to Exportability team.")
    }

    "Not create an assisted decision section if person normally lives in England, Scotland or Wales" in {
      val residency = NationalityAndResidency(resideInUK = YesNoWithText("yes"))
      val claim = Claim().update(residency)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecisions").length mustEqual 0
    }

  } section "unit"

}
