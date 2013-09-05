package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Employment => Employed, _}
import controllers.Mappings._
import models.{PaymentFrequency, DayMonthYear}

class EmploymentSpec extends Specification with Tags {

  "Employment" should {

    val startDate = DayMonthYear(Some(1), Some(1), Some(2000))
    val endDate = DayMonthYear(Some(1), Some(1), Some(2005))

    "generate xml when data is present" in {
      val employerName = "KFC"
      val hours = "70"
      val jobDetails = JobDetails("1", employerName, startDate, "no", Some(endDate), Some(endDate), Some(hours), None)
      val jobs = Jobs(List(Job("1", jobDetails :: Nil)))

      val claim = Claim().update(Employed(beenEmployedSince6MonthsBeforeClaim = yes))
                         .update(jobs)

      val employment = Employment.xml(claim)

      (employment \\ "CurrentlyEmployed").text mustEqual yes

      val employer = employment \\ "Employer"
      (employer \\ "Name").text mustEqual employerName
      (employer \\ "DateJobStarted").text mustEqual startDate.`yyyy-MM-dd`
      (employer \\ "DateJobEnded").text mustEqual endDate.`yyyy-MM-dd`
      (employer \\ "ClockPayrollNumber").text must beEmpty

      val pay = employment \\ "Pay"
      (pay \\ "WeeklyHoursWorked").text mustEqual hours
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(Employed(beenEmployedSince6MonthsBeforeClaim = no))
      val employment = Employment.xml(claim)
      employment.text must beEmpty
    }

    "generate <PensionScheme> if claimer has paid for occupational pension scheme" in {
      val amount = "200"
      val pensionScheme = Job("1", List(PensionSchemes(payOccupationalPensionScheme = "yes", howMuchPension = Some(amount), howOftenPension = Some(PaymentFrequency("weekly")))))

      val pensionSchemeXml = Employment.pensionSchemeXml(pensionScheme)
      (pensionSchemeXml \\ "PaidForOccupationalPension").text mustEqual "yes"
      (pensionSchemeXml \\ "PensionScheme" \\ "Payment" \\ "Amount").text shouldEqual amount
      (pensionSchemeXml \\ "PensionScheme" \\ "Frequency").text shouldEqual "Weekly"
      (pensionSchemeXml \\ "PaidForPersonalPension").text must beEmpty
    }

    "generate <PensionScheme> if claimer has paid for personal pension scheme" in {
      val amount = "200"
      val pensionScheme = Job("1", List(PensionSchemes(payPersonalPensionScheme = "yes", howMuchPersonal = Some(amount), howOftenPersonal = Some("W"))))

      val pensionSchemeXml = xml.Employment.pensionSchemeXml(pensionScheme)
      (pensionSchemeXml \\ "PaidForPersonalPension").text mustEqual "yes"
      (pensionSchemeXml \\ "PensionScheme" \\ "Payment" \\ "Amount").text shouldEqual amount
      (pensionSchemeXml \\ "PensionScheme" \\ "Frequency").text shouldEqual "W"
      (pensionSchemeXml \\ "PaidForOccupationalPension").text must beEmpty
    }

    "skip <PensionScheme> if claimer has NO pension scheme" in {
      val pensionSchemeXml = Employment.pensionSchemeXml(Job("1", List()))

      (pensionSchemeXml \\ "PaidForOccupationalPension").text must beEmpty
      (pensionSchemeXml \\ "PaidForPersonalPension").text must beEmpty
      (pensionSchemeXml \\ "PensionScheme").isEmpty should beTrue
    }

    "generate <ChildCareExpenses> if claimer pays anyone to care for children" in {
      val childcareCarer = "Mark"
      val address = "someAddress"
      val postcode = "M1"
      val amount = "500"
      val relation = "brother"
      val relationToChild = "uncle"
      val job = Job("1", List(
                  AboutExpenses(payAnyoneToLookAfterChildren = "yes"),
                  ChildcareExpenses(whoLooksAfterChildren = childcareCarer, howMuchCostChildcare = amount, relationToYou = relation, relationToPersonYouCare = relationToChild)
      ))

      val childcareXml = Employment.childcareExpensesXml(job)

      (childcareXml \\ "CareExpensesChildren").text shouldEqual "yes"

      val expenses = childcareXml \\ "ChildCareExpenses"
      (expenses \\ "CarerName").text shouldEqual childcareCarer
      (expenses \\ "WeeklyPayment" \\ "Amount").text shouldEqual amount
      (expenses \\ "RelationshipCarerToClaimant").text shouldEqual relation
      (expenses \\ "ChildDetails" \\ "RelationToChild").text shouldEqual relationToChild
    }

    "skip <ChildCareExpenses> if claimer has NO childcare expenses" in {
      val job = Job("1", List(AboutExpenses(payAnyoneToLookAfterChildren = "no")))

      val childcareXml = Employment.childcareExpensesXml(job)
      (childcareXml \\ "CareExpensesChildren").text mustEqual "no"
      (childcareXml \\ "ChildCareExpenses").isEmpty must beTrue
    }

    "generate <CareExpenses> if claimer has care expenses" in {
      val relation = "other"
      val carer = "someGipsy"
      val amount = "300"
      val howOftenPayCare = "02"
      val job = Job("1", List(
        AboutExpenses(payAnyoneToLookAfterPerson = "yes"),
        PersonYouCareForExpenses(whoDoYouPay = carer, howMuchCostCare = amount, howOftenPayCare = howOftenPayCare, relationToYou = relation, relationToPersonYouCare = relation)
      ))

      val careExpensesXml = Employment.careExpensesXml(job)

      (careExpensesXml \\ "CareExpensesCaree").text shouldEqual "yes"

      val expenses = careExpensesXml \\ "CareExpenses"
      (expenses \\ "CarerName").text shouldEqual carer
      (expenses \\ "WeeklyPayment" \\ "Amount").text shouldEqual amount
      (expenses \\ "RelationshipCarerToClaimant").text shouldEqual relation
      (expenses \\ "RelationshipCarerToCaree").text shouldEqual relation
    }

    "skip <CareExpenses> if claimer has NO care expenses" in {
      val job = Job("1",List(
        AboutExpenses(payAnyoneToLookAfterPerson = "no")
      ))

      val careExpensesXml = Employment.careExpensesXml(job)

      (careExpensesXml \\ "CareExpensesCaree").text shouldEqual "no"
      (careExpensesXml \\ "CareExpenses").isEmpty must beTrue
    }
  } section "unit"
}