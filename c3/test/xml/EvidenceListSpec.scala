package xml

import models.domain._
import org.specs2.mutable.{Tags, Specification}
import models.domain.Claim
import app.StatutoryPaymentFrequency
import models.PaymentFrequency


class EvidenceListSpec extends Specification with Tags {

  "Assisted section" should {

    "Create a assisted decision section if care less than 35 hours" in {
      val moreAboutTheCare = MoreAboutTheCare("no")
      val claim = Claim().update(moreAboutTheCare)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Do not spend 35 hours or more each week caring. NIL decision, but need to check advisory additional notes.")
    }

    "Create a assisted decision section if employment with £100.01 a week and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "100.01")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create a assisted decision section if employment with £200.02 a fortnight and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "200.02")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Fortnightly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create a assisted decision section if employment with £400.04 every 4 weeks and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "400.04")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create a assisted decision section if employment with £433.38 a month and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage = LastWage(jobID = "12", grossPay = "433.38")
      val additionalWage = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Monthly)))
      val job = Job("12",List(lastWage,additionalWage))
      val jobs = Jobs(List(job))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create a assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and no expenses, pension schemes." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "50")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val job2 = Job("13",List(lastWage2,additionalWage2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Create a assisted decision section if employment with 2 jobs one £100.01 a week and one 200.04 with no frequency." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "100.01")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "200.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13")
      val job2 = Job("13",List(lastWage2,additionalWage2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must contain("Total weekly gross pay 100.01 > £100. NIL decision, but need to check advisory additional notes.")
    }

    "Does not create a assisted decision section if employment with 2 jobs one £50 a week and one 200.04 four-weekly and job 2 has an expense." in {
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
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must not contain("NIL decision, but need to check advisory additional notes.")
    }

    "Does not create a assisted decision section if employment with 2 jobs one £50 a week and one 100.04 four-weekly." in {
      val moreAboutTheCare = MoreAboutTheCare("yes")
      val lastWage1 = LastWage(jobID = "12", grossPay = "50")
      val additionalWage1 = AdditionalWageDetails(jobID = "12",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.Weekly)))
      val job1 = Job("12",List(lastWage1,additionalWage1))
      val lastWage2 = LastWage(jobID = "13", grossPay = "100.04")
      val additionalWage2 = AdditionalWageDetails(jobID = "13",oftenGetPaid = Some(PaymentFrequency(StatutoryPaymentFrequency.FourWeekly)))
      val job2 = Job("13",List(lastWage2,additionalWage2))
      val jobs = Jobs(List(job1,job2))
      val claim = Claim().update(moreAboutTheCare).update(jobs)
      val xml = EvidenceList.assistedDecision(claim)
      (xml \\ "TextLine").text must not contain("NIL decision, but need to check advisory additional notes.")
    }


  } section "unit"

}
