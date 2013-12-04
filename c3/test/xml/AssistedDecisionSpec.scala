package xml

import models.domain._
import org.specs2.mutable.{Tags, Specification}
import app.StatutoryPaymentFrequency
import models.{DayMonthYear, PaymentFrequency}
import org.joda.time.DateTime
import models.PaymentFrequency
import models.domain.Claim
import scala.Some


class AssistedDecisionSpec extends Specification with Tags {

  "Assisted section" should {

    "Create an assisted decision section if care less than 35 hours" in {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val claim = Claim().update(moreAboutTheCare)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Do not spend 35 hours or more each week caring. NIL decision, but need to check advisory additional notes.")
    }

    "Not create an assisted decision section if care more than 35 hours" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val claim = Claim().update(moreAboutTheCare)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Do not spend 35 hours or more each week caring. NIL decision, but need to check advisory additional notes."
    }

    "Create an assisted decision section if employment with £100.01 a week and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "100.01")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create an assisted decision section if employment with £200.02 a fortnight and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "200.02")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Fortnightly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create an assisted decision section if employment with £400.04 every 4 weeks and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "400.04")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create an assisted decision section if employment with £433.38 a month and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "433.38")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Monthly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create an assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "50")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val job2 = Job("13",List(lastWage2,additionalWage2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create an assisted decision section if employment with 2 jobs one £100.01 a week and one 200.04 with no frequency." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "100.01")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13")
      val job2 = Job("13",List(lastWage2,additionalWage2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Not create an assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and job 2 has an expense." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "50")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val expense2 = ChildcareExpenses(jobID = "13", howMuchCostChildcare = "2")
      val job2 = Job("13",List(lastWage2,additionalWage2,expense2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "NIL decision, but need to check advisory additional notes."
    }

    "Not create an assisted decision section if employment with 2 jobs one £50 a week and one 100.04 four-weekly." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "50")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "100.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val job2 = Job("13",List(lastWage2,additionalWage2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "NIL decision, but need to check advisory additional notes."
    }

    "Create an assisted decision section if less than 16 years old" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val now = DateTime.now()
      val details = YourDetails(dateOfBirth = DayMonthYear(now.minusYears(16).plusDays(1)))
      val claim = Claim().update(moreAboutTheCare).update(details)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Customer Date of Birth")
    }

    "Not create an assisted decision section if care 16 years old" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val now = DateTime.now()
      val details = YourDetails(dateOfBirth = DayMonthYear(now.minusYears(16).minusDays(1)))
      val claim = Claim().update(moreAboutTheCare).update(details)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Customer Date of Birth"
    }

    "Create an assisted decision section if AFIP is yes" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val moreAboutThePerson = MoreAboutThePerson(armedForcesPayment="yes")
      val claim = Claim().update(moreAboutTheCare).update(moreAboutThePerson)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Person receives Armed Forces Independence Payment. Transfer to Armed Forces Independent Payments team.")
    }

    "Not create an assisted decision section if AFIP is no" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val moreAboutThePerson = MoreAboutThePerson(armedForcesPayment="no")
      val claim = Claim().update(moreAboutTheCare).update(moreAboutThePerson)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Person receives Armed Forces Independence Payment. Transfer to Armed Forces Independent Payments team."
    }

    "Create an assisted decision section if EEA pension" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(benefitsFromOtherEEAStateOrSwitzerland="yes")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Claimant or partner dependent on EEA pensions or benefits. Transfer to Exportability team or potential disallowance.")
    }

    "Not create an assisted decision section if no EEA pension" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(benefitsFromOtherEEAStateOrSwitzerland="no")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Claimant or partner dependent on EEA pensions or benefits. Transfer to Exportability team or potential disallowance."
    }

    "Create an assisted decision section if EEA insurance or working" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(workingForOtherEEAStateOrSwitzerland="yes")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must contain("Claimant or partner dependent on EEA insurance or work. Transfer to Exportability team or potential disallowance.")
    }

    "Not create an assisted decision section if no EEA insurance or working" in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(workingForOtherEEAStateOrSwitzerland="no")
      val claim = Claim().update(moreAboutTheCare).update(otherEEAStateOrSwitzerland)
      val xml = AssistedDecision.xml(claim)
      (xml \\ "TextLine").text must not contain "Claimant or partner dependent on EEA insurance or work. Transfer to Exportability team or potential disallowance."
    }

  } section "unit"

}
