package app.preview

import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import utils.pageobjects._
import app.FunctionalTestCommon
import utils.pageobjects.preview.PreviewPage


class FunctionalTestCase4Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {

    "Successfully run absolute Claim Test Case 4" in new WithBrowser with PageObjects {
      pending
      import Data.displaying

      val page = G1BenefitsPage(context)
      implicit val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase4.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, PreviewPage.title)

      val toFindData = Data.build(
        "Name"              displays ("AboutYouTitle","AboutYouFirstName","AboutYouMiddleName","AboutYouSurname"),
        "National Insurance number" displays "AboutYouNINO",
        "Date of birth"     displays DateTransformer("AboutYouDateOfBirth"),
        "Address"           displays (AddressTransformer("AboutYouAddress"),"AboutYouPostcode"),
        "Your claim date"   displays DateTransformer("AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart"),
        "What is your nationality?"  displays "AboutYouNationalityAndResidencyNationality",
        "Enter you nationality"  displays "AboutYouNationalityAndResidencyNationality",
        "Time outside of England, Scotland or Wales"          displays AnyYesTransformer("AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime"),
        "Have you or anyone in your family claimed or been paid any benefits or pensions from any of these countries?"          displays "OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA",
        "Do you or anyone in your family work or pay insurance in any of these countries?" displays "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA",
        "Marital status"  displays MaritalTransformer("AboutYouWhatIsYourMaritalOrCivilPartnershipStatus"),
        "Name"            displays ("AboutTheCareYouProvideTitlePersonCareFor","AboutTheCareYouProvideFirstNamePersonCareFor","AboutTheCareYouProvideMiddleNamePersonCareFor","AboutTheCareYouProvideSurnamePersonCareFor"),
        "Date of birth"   displays DateTransformer("AboutTheCareYouProvideDateofBirthPersonYouCareFor"),
        "Address"         displays (AddressTransformer("AboutTheCareYouProvideAddressPersonCareFor"),"AboutTheCareYouProvidePostcodePersonCareFor"),
        "What's their relationship to you?"                               displays "AboutTheCareYouProvideWhatTheirRelationshipToYou",
        "Do you spend 35 hours or more each week caring for this person?" displays "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek",
        "Have you had any breaks in caring for this person"               displays (AnyYesTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare"),NumDetailsProvidedTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare")),
        "Have you been on a course of education since your claim date?"   displays "AboutYouHaveYouBeenOnACourseOfEducation",
        "Have you been employed at any time since"      displays "EmploymentHaveYouBeenEmployedAtAnyTime_0",
        "Employment"                                    displays EmploymentDetailsTransformer("EmploymentEmployerName",1),
        "Have you been self employed at any time since" displays "EmploymentHaveYouBeenSelfEmployedAtAnyTime",
        "Have you  claimed or received any other benefits since the date you want to claim"                    displays "OtherMoneyHaveYouClaimedOtherBenefits",
        "Have you received any payments for the person you care for or any other person since your claim date" displays "OtherMoneyAnyPaymentsSinceClaimDate",
        "Have you had any Statutory Sick Pay"                    displays "OtherMoneyHaveYouSSPSinceClaim",
        "Have you had any SMP, SPP or SAP since your claim date" displays "OtherMoneyHaveYouSMPSinceClaim",
        "Payment method" displays PaymentTransformer("HowWePayYouHowWouldYouLikeToGetPaid")
      )

      toFindData.assertReview(claim, context) must beTrue

    }

  } section ("functional","preview")
}



