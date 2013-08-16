package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import controllers.Mappings._
import models.DayMonthYear
import models.MultiLineAddress

class EmploymentSpec extends Specification with Tags {

  "Employment" should {

    val startDate = DayMonthYear(Some(1), Some(1), Some(2000))
    val endDate = DayMonthYear(Some(1), Some(1), Some(2005))

    "generate xml when data is present" in {
      val employerName = "KFC"
      val jobType = "Chicken feeder"
      val hours = "70"
      val jobDetails = Jobs(List(
                           Job("1", List(
                             JobDetails("1", employerName, Some(startDate), "no", Some(endDate), Some(hours), Some(jobType), None)))))

      val claim = Claim().update(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = yes))
        .update(jobDetails)

      val employment = xml.Employment.xml(claim)

      (employment \\ "CurrentlyEmployed").text mustEqual yes
      val employer = employment \\ "Employer"
      (employer \\ "Name").text mustEqual employerName
      (employer \\ "DateJobStarted").text mustEqual startDate.`yyyy-MM-dd`
      (employer \\ "DateJobEnded").text mustEqual endDate.`yyyy-MM-dd`
      (employer \\ "JobType").text mustEqual jobType
      (employer \\ "ClockPayrollNumber").text must beEmpty
      val pay = employment \\ "Pay"
      (pay \\ "WeeklyHoursWorked").text mustEqual hours
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(models.domain.Employment(beenEmployedSince6MonthsBeforeClaim = no))
      val employment = xml.Employment.xml(claim)
      employment.text must beEmpty
    }

    "generate <PensionScheme> if claimer has paid for occupational pension scheme" in {
      val amount = "200"
      val pensionScheme = Job("1",List(PensionSchemes(payOccupationalPensionScheme = "yes", howMuchPension = Some(amount), howOftenPension = Some("W"))))

      val pensionSchemeXml = xml.Employment.pensionSchemeXml(pensionScheme)

      (pensionSchemeXml \\ "PaidForOccupationalPension").text mustEqual "yes"
      (pensionSchemeXml \\ "PensionScheme" \\ "Payment" \\ "Amount").text shouldEqual amount
      (pensionSchemeXml \\ "PensionScheme" \\ "Frequency").text shouldEqual "W"
      (pensionSchemeXml \\ "PaidForPersonalPension").text must beEmpty
    }

    "generate <PensionScheme> if claimer has paid for personal pension scheme" in {
      val amount = "200"
      val pensionScheme = Job("1",List(PensionSchemes(payPersonalPensionScheme = "yes", howMuchPersonal = Some(amount), howOftenPersonal = Some("W"))))

      val pensionSchemeXml = xml.Employment.pensionSchemeXml(pensionScheme)

      (pensionSchemeXml \\ "PaidForPersonalPension").text mustEqual "yes"
      (pensionSchemeXml \\ "PensionScheme" \\ "Payment" \\ "Amount").text shouldEqual amount
      (pensionSchemeXml \\ "PensionScheme" \\ "Frequency").text shouldEqual "W"
      (pensionSchemeXml \\ "PaidForOccupationalPension").text must beEmpty
    }

    "skip <PensionScheme> if claimer has NO pension scheme" in {
      val pensionSchemeXml = xml.Employment.pensionSchemeXml(Job("1", List()))

      (pensionSchemeXml \\ "PaidForOccupationalPension").text must beEmpty
      (pensionSchemeXml \\ "PaidForPersonalPension").text must beEmpty
      (pensionSchemeXml \\ "PensionScheme").isEmpty should beTrue
    }

    "generate <ChildCareExpenses> if claimer pays anyone to look after children" in {
      val childcareCarer = "Mark"
      val address = "someAddress"
      val postcode = "M1"
      val amount = "500"
      val relation = "brother"
      val relationToChild = "uncle"
      val job = Job("1", List(
                  AboutExpenses(payAnyoneToLookAfterChildren = "yes"),
                  ChildcareExpenses(howMuchCostChildcare = Some(amount), whoLooksAfterChildren = childcareCarer, relationToYou = relation, relationToPersonYouCare = relationToChild),
                  ChildcareProvider(address = Some(MultiLineAddress(Some(address))), postcode = Some(postcode))
      ))

      val childcareXml = xml.Employment.childcareExpensesXml(job)

      (childcareXml \\ "CareExpensesChildren").text shouldEqual "yes"
      val expenses = childcareXml \\ "ChildCareExpenses"
      (expenses \\ "CarerName").text shouldEqual childcareCarer
      (expenses \\ "CarerAddress" \\ "Line").theSeq(0).text shouldEqual address
      (expenses \\ "CarerAddress" \\ "PostCode").text shouldEqual postcode
      (expenses \\ "WeeklyPayment" \\ "Amount").text shouldEqual amount
      (expenses \\ "RelationshipCarerToClaimant").text shouldEqual relation
      (expenses \\ "ChildDetails" \\ "RelationToChild").text shouldEqual relationToChild
    }

    "skip <ChildCareExpenses> if claimer has NO childcare expenses" in {
      val job = Job("1", List(AboutExpenses(payAnyoneToLookAfterChildren = "no")))

      val childcareXml = xml.Employment.childcareExpensesXml(job)
      (childcareXml \\ "CareExpensesChildren").text mustEqual "no"
      (childcareXml \\ "ChildCareExpenses").isEmpty must beTrue
    }

    "generate <CareExpenses> if claimer has care expenses" in {
      val address = "someAddress"
      val postcode = "M1"
      val relation = "other"
      val carer = "someGipsy"
      val amount = "300"
      val job = Job("1", List(
        AboutExpenses(payAnyoneToLookAfterPerson = "yes"),
        PersonYouCareForExpenses(howMuchCostCare = Some(amount), whoDoYouPay = carer, relationToYou = relation, relationToPersonYouCare = relation),
        CareProvider(address = Some(MultiLineAddress(Some(address))), postcode = Some(postcode))
      ))

      val careExpensesXml = xml.Employment.careExpensesXml(job)

      (careExpensesXml \\ "CareExpensesCaree").text shouldEqual "yes"
      val expenses = careExpensesXml \\ "CareExpenses"
      (expenses \\ "CarerName").text shouldEqual carer
      (expenses \\ "CarerAddress" \\ "Line").theSeq(0).text shouldEqual address
      (expenses \\ "CarerAddress" \\ "PostCode").text shouldEqual postcode
      (expenses \\ "WeeklyPayment" \\ "Amount").text shouldEqual amount
      (expenses \\ "RelationshipCarerToClaimant").text shouldEqual relation
      (expenses \\ "RelationshipCarerToCaree").text shouldEqual relation
    }

    "skip <CareExpenses> if claimer has NO care expenses" in {
      val job = Job("1",List(
        AboutExpenses(payAnyoneToLookAfterPerson = "no")
      ))

      val careExpensesXml = xml.Employment.careExpensesXml(job)

      (careExpensesXml \\ "CareExpensesCaree").text shouldEqual "no"
      (careExpensesXml \\ "CareExpenses").isEmpty must beTrue
    }
  } section "unit"
}