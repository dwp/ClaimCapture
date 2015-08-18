package app.preview

import utils.WithJsBrowser
import utils.pageobjects.s_eligibility.GBenefitsPage
import utils.pageobjects._
import app.FunctionalTestCommon
import utils.pageobjects.preview.PreviewPage


class FunctionalTestCase3Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {

    "Successfully run absolute Claim Test Case 3" in new WithJsBrowser with PageObjects {
      import Data.displaying

      val page = GBenefitsPage(context)
      implicit val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase3.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, PreviewPage.url)
      val toFindData = Data.build(
        "Name"              displays ("AboutYouTitle","AboutYouFirstName","AboutYouMiddleName","AboutYouSurname"),
        "Date of birth"     displays DateTransformer("AboutYouDateOfBirth"),
        "Address"           displays (AddressTransformer("AboutYouAddress"),"AboutYouPostcode"),
        "Claim date"   displays DateTransformer("ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart"),
        "Your nationality"  displays "AboutYouNationalityAndResidencyNationality",
        "Do you normally live in England, Scotland or Wales?" displays "AboutYouNationalityAndResidencyResideInUK",
        "Have you been away from England, Scotland or Wales"          displays AnyYesTransformer("AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime"),
        "Have you or anyone in your close family claimed or been paid any benefits or pensions from an EEA country since your claim date?"          displays "OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA",
        "Have you or anyone in your close family worked or paid national insurance in an EEA country since your claim date?" displays "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA",
        "Name"            displays ("AboutYourPartnerTitle","AboutYourPartnerFirstName","AboutYourPartnerMiddleName","AboutYourPartnerSurname"),
        "Date of birth"   displays DateTransformer("AboutYourPartnerDateofBirth"),
        "Have you separated since your claim date?" displays "AboutYourPartnerHaveYouSeparatedfromYourPartner",
        "Do they live at the same address as you?"         displays "AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou",
        "What's their relationship to you?"                               displays "AboutTheCareYouProvideWhatTheirRelationshipToYou",
        "Do you spend 35 hours or more each week caring for this person?" displays "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek",
        "Have you had any breaks in caring for this person"               displays (AnyYesTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare"),NumDetailsProvidedTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare")),
        "Have you been on a course of education since your claim date?"   displays "EducationHaveYouBeenOnACourseOfEducation",
        "Have you been employed at any time since"      displays "EmploymentHaveYouBeenEmployedAtAnyTime_0",
        "Have you been self employed at any time since" displays "EmploymentHaveYouBeenSelfEmployedAtAnyTime",
        "Have you received any payments for the person you care for or any other person since your claim date?" displays "OtherMoneyAnyPaymentsSinceClaimDate",
        "Have you had any Statutory Sick Pay"                    displays "OtherMoneyHaveYouSSPSinceClaim",
        "Have you had any Statutory Maternity Pay, Statutory Paternity Pay or Statutory Adoption Pay" displays "OtherMoneyHaveYouSMPSinceClaim"
      )

      toFindData.assertReview(claim, context) must beTrue

    }

  } section ("functional","preview")
}



