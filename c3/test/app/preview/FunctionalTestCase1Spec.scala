package app.preview

import utils.WithJsBrowser
import utils.pageobjects.s_eligibility.GBenefitsPage
import utils.pageobjects._
import app.FunctionalTestCommon
import utils.pageobjects.preview.PreviewPage


class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  "The application Claim" should {

    "Successfully run absolute Claim Test Case 1" in new WithJsBrowser with PageObjects {
      import Data.displaying

      val page = GBenefitsPage(context)
      implicit val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, PreviewPage.url)
      val toFindData = Data.build(
        "Name"              displays ("AboutYouTitle","AboutYouFirstName","AboutYouMiddleName","AboutYouSurname"),
        "Date of birth"     displays DateTransformer("AboutYouDateOfBirth"),
        "Address"           displays (AddressTransformer("AboutYouAddress"),"AboutYouPostcode"),
        "Have you got an email address" displays ("AboutYouWantsEmailContact", "AboutYouMail"),
        "Claim date"   displays DateTransformer("ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart"),
        "Your nationality"  displays "AboutYouNationalityAndResidencyActualNationality",
        "Do you normally live in England, Scotland or Wales?" displays "AboutYouNationalityAndResidencyResideInUK",
        "Marital status"  displays "AboutYouWhatIsYourMaritalOrCivilPartnershipStatus",
        "Have you been away from England, Scotland or Wales"          displays AnyYesTransformer("AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime"),
        "Have you or any of your close family worked abroad or been paid benefits from outside the United Kingdom since your claim date?"          displays "OtherMoneyOtherEEAGuardQuestion",
        "Name"            displays ("AboutTheCareYouProvideTitlePersonCareFor","AboutTheCareYouProvideFirstNamePersonCareFor","AboutTheCareYouProvideMiddleNamePersonCareFor","AboutTheCareYouProvideSurnamePersonCareFor"),
        "Date of birth"   displays DateTransformer("AboutTheCareYouProvideDateofBirthPersonYouCareFor"),
        "What's their relationship to you?"                               displays "AboutTheCareYouProvideWhatTheirRelationshipToYou",
        "Do you spend 35 hours or more each week caring for this person?" displays "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek",
        "Have you had any breaks from caring for this person since 21 January 2013"               displays AnyYesTransformer("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare"),
        "Have you been on a course of education since your claim date?"   displays "EducationHaveYouBeenOnACourseOfEducation",
        "Have you been employed at any time since 21 January 2013"      displays "EmploymentHaveYouBeenEmployedAtAnyTime_0",
        "Have you been self employed at any time since" displays "EmploymentHaveYouBeenSelfEmployedAtAnyTime",
        "Have you received any payments for the person you care for or any other person since your claim date?" displays "OtherMoneyAnyPaymentsSinceClaimDate",
        "Have you had any Statutory Sick Pay"                    displays "OtherMoneyHaveYouSSPSinceClaim",
        "Have you had any Statutory Maternity Pay, Statutory Paternity Pay or Statutory Adoption Pay" displays "OtherMoneyHaveYouSMPSinceClaim",
        "Do you want to tell us any additional information about your claim?" displays "ConsentDeclarationTellUsAnythingElseAnswerAboutClaim",
        "Do you live in Wales and want to receive future communications in Welsh?" displays "ConsentDeclarationCommunicationWelsh"
      )
      toFindData.assertReview(claim, context) must beTrue
    }
  }
  section ("functional","preview")
}



