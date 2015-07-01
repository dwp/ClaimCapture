package app.preview

import utils.WithJsBrowser
import utils.pageobjects.s0_carers_allowance.G1BenefitsPage
import utils.pageobjects._
import app.FunctionalTestCommon
import utils.pageobjects.preview.PreviewPage


class FunctionalTestCase2Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {

    "Successfully run absolute Claim Test Case 2" in new WithJsBrowser with PageObjects {
      import Data.displaying

      val page = G1BenefitsPage(context)
      implicit val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase2.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, PreviewPage.url)
      val toFindData = Data.build(
        "Name"              displays ("AboutYouTitle","AboutYouFirstName","AboutYouMiddleName","AboutYouSurname"),
        "National Insurance number" displays "AboutYouNINO",
        "Date of birth"     displays DateTransformer("AboutYouDateOfBirth"),
        "Address"           displays (AddressTransformer("AboutYouAddress"),"AboutYouPostcode"),
        "Your claim date"   displays DateTransformer("ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart"),
        "Your nationality"  displays "AboutYouNationalityAndResidencyNationality",
        "Time outside of England, Scotland or Wales"          displays AnyYesTransformer("AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime"),
        "Have you or anyone in your family claimed or been paid any benefits or pensions from any of these countries since your claim date?"          displays "OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA",
        "Have you or your close family worked or paid national insurance in any of these countries since your claim date?" displays "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA",
        "Name"            displays ("AboutYourPartnerTitle","AboutYourPartnerFirstName","AboutYourPartnerMiddleName","AboutYourPartnerSurname"),
        "National Insurance number" displays "AboutYourPartnerNINO",
        "Date of birth"   displays DateTransformer("AboutYourPartnerDateofBirth"),
        "Your nationality"displays "AboutYouNationalityAndResidencyNationality",
        "Have you separated since your claim date?" displays "AboutYourPartnerHaveYouSeparatedfromYourPartner",
        "Name"            displays ("AboutTheCareYouProvideTitlePersonCareFor","AboutTheCareYouProvideFirstNamePersonCareFor","AboutTheCareYouProvideMiddleNamePersonCareFor","AboutTheCareYouProvideSurnamePersonCareFor"),
        "Date of birth"   displays DateTransformer("AboutTheCareYouProvideDateofBirthPersonYouCareFor"),
        "Address"         displays (AddressTransformer("AboutTheCareYouProvideAddressPersonCareFor"),"AboutTheCareYouProvidePostcodePersonCareFor"),
        "What's their relationship to you?"                               displays "AboutTheCareYouProvideWhatTheirRelationshipToYou",
        "Do you spend 35 hours or more each week caring for this person?" displays "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek",
        "Have you had any breaks in caring for this person"               displays AnyYesTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare"),
        "Have you been on a course of education since your claim date?"   displays "EducationHaveYouBeenOnACourseOfEducation",
        "Have you been employed at any time since"      displays "EmploymentHaveYouBeenEmployedAtAnyTime_0",
        "Have you been self employed at any time since" displays "EmploymentHaveYouBeenSelfEmployedAtAnyTime",
        "Have you received any payments for the person you care for or any other person since your claim date?" displays "OtherMoneyAnyPaymentsSinceClaimDate",
        "Have you had any Statutory Sick Pay"                    displays "OtherMoneyHaveYouSSPSinceClaim",
        "Have you had any SMP, SPP or SAP since your claim date" displays "OtherMoneyHaveYouSMPSinceClaim"
      )

      toFindData.assertReview(claim, context) must beTrue

    }

  } section ("functional","preview")
}



