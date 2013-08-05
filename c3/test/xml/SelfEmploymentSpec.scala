package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import controllers.Mappings._
import models.{DayMonthYear}
import models.MultiLineAddress
import scala.Some
import models.yesNo.YesNoWithText

class SelfEmploymentSpec extends Specification with Tags {

  "SelfEmployment" should {

    val startDate = DayMonthYear(Some(1), Some(1), Some(2000))
    val endDate = DayMonthYear(Some(1), Some(1), Some(2005))
    val software = "software"
    val amount = "15.5"

    "generate xml when data is present" in {
      val aboutSelfEmployment =  AboutSelfEmployment(areYouSelfEmployedNow = yes,
        whenDidYouStartThisJob=Some(startDate),
        whenDidTheJobFinish=Some(endDate),
        haveYouCeasedTrading = Some(no),
        natureOfYourBusiness = Some(software)
      )

      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = yes))
      .update(aboutSelfEmployment)

      val selfEmploymentXml = xml.SelfEmployment.xml(claim)

      (selfEmploymentXml \\ "SelfEmployedNow").text mustEqual yes
      val recentJobDetailsXml = selfEmploymentXml \\ "RecentJobDetails"
      (recentJobDetailsXml \\ "DateStarted").text mustEqual startDate.`yyyy-MM-dd`
      (recentJobDetailsXml \\ "DateEnded").text mustEqual endDate.`yyyy-MM-dd`
      (recentJobDetailsXml \\ "NatureOfBusiness").text mustEqual software
      (recentJobDetailsXml \\ "TradingCeased").text mustEqual no
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(Employment(beenSelfEmployedSince1WeekBeforeClaim = no))
      val selfEmploymentXml = xml.SelfEmployment.xml(claim)
      selfEmploymentXml.text mustEqual ""
    }

    "generate <AccountantDetails> xml if claimer has an accountant" in {
      val yourAccounts =  SelfEmploymentYourAccounts(doYouHaveAnAccountant=Some(yes))
      val accountantName = "accountantName"
      val address = MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))
      val postcode = Some("SE1 6EH")
      val telephoneNumber = Some("020 029381273")
      val faxNumber = Some("020 888999222")
      val accountantContactDetails = SelfEmploymentAccountantContactDetails(accountantsName=accountantName, address=address, postcode = postcode, telephoneNumber=telephoneNumber, faxNumber=faxNumber)

      val claim = Claim().update(yourAccounts)
        .update(accountantContactDetails)

      val accountantDetailsXml = xml.SelfEmployment.accountantDetails(claim)

      (accountantDetailsXml \\ "Name").text shouldEqual accountantName
      (accountantDetailsXml \\ "Address" \\ "PostCode").text shouldEqual postcode.get
      (accountantDetailsXml \\ "PhoneNumber").text shouldEqual telephoneNumber.get
      (accountantDetailsXml \\ "FaxNumber").text shouldEqual faxNumber.get
    }

    "skip <AccountantDetails> xml if claimer" in {
      "answered no to 'Do you have an accountant?'" in {
        val yourAccounts =  SelfEmploymentYourAccounts(doYouHaveAnAccountant=Some(no))
        val claim = Claim().update(yourAccounts)

        val accountantDetailsXml = xml.SelfEmployment.accountantDetails(claim)
        accountantDetailsXml.text shouldEqual ""
      }
      "didn't answer 'Do you have an accountant?'" in {
        val accountantDetailsXml = xml.SelfEmployment.accountantDetails(Claim())
        accountantDetailsXml.text shouldEqual ""
      }
    }

    "generate <PensionScheme> if claimer has paid for pension scheme" in {
       val pensionScheme = SelfEmploymentPensionsAndExpenses(pensionSchemeMapping=YesNoWithText(yes, Some(amount)))
       val claim = Claim().update(pensionScheme)

       val pensionSchemeXml = xml.SelfEmployment.pensionScheme(claim)

      (pensionSchemeXml \\ "Payment" \\ "Amount").text shouldEqual amount
    }

    "skip <PensionScheme> if claimer has no pension scheme" in {
      val pensionSchemeXml = xml.SelfEmployment.pensionScheme(Claim())

      pensionSchemeXml.text shouldEqual ""
    }

    "generate <ChildCareExpenses> if claimer pays anyone to look after children" in {

      val pensionScheme = SelfEmploymentPensionsAndExpenses(lookAfterChildrenMapping=YesNoWithText(yes, Some(amount)))
      val childcareExpenses = ChildcareExpensesWhileAtWork(howMuchYouPay = Some(amount), nameOfPerson = "Andy", whatRelationIsToYou = Some("grandSon"), whatRelationIsTothePersonYouCareFor = Some("relation"))
      val claim = Claim().update(pensionScheme).update(childcareExpenses)

      val childcareXml = xml.SelfEmployment.childCareExpenses(claim)
      (childcareXml \\ "CarerName").text shouldEqual childcareExpenses.nameOfPerson
      (childcareXml \\ "WeeklyPayment" \\ "Amount").text shouldEqual amount
      (childcareXml \\ "RelationshipCarerToClaimant").text  shouldEqual childcareExpenses.whatRelationIsToYou.get
      (childcareXml \\ "ChildDetails" \\ "RelationToChild").text shouldEqual childcareExpenses.whatRelationIsTothePersonYouCareFor.get
    }

    "skip <ChildCareExpenses> if claimer has no childcare expenses" in {
      val pensionScheme = SelfEmploymentPensionsAndExpenses(lookAfterChildrenMapping=YesNoWithText(no, Some(amount)))
      val claim = Claim().update(pensionScheme)
      val childcareXml = xml.SelfEmployment.childCareExpenses(claim)
      childcareXml.text shouldEqual ""
    }

  } section "unit"

}