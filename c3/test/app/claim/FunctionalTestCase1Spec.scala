package app.claim

import app.FunctionalTestCommon
import utils.WithJsBrowser
import utils.pageobjects._
import utils.pageobjects.preview.PreviewTestableData
import utils.pageobjects.s_eligibility.GBenefitsPage

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class FunctionalTestCase1Spec extends FunctionalTestCommon {
  isolated

  section("functional", "claim")
  "The application Claim" should {
    "Successfully run absolute Claim Test Case 1" in new WithJsBrowser with PageObjects {
      val page = GBenefitsPage(context)
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      test(page, claim, buildPreviewUseData)
    }
  }
  section("functional", "claim")


  private def buildPreviewUseData = {

    PreviewTestableData()   +
      "AboutYouTitle" + "AboutYouFirstName" + "AboutYouMiddleName" + "AboutYouSurname" +
      dateConversion("AboutYouDateOfBirth") +
      dateConversion("ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart") +
      addressConversion("AboutYouAddress") + "AboutYouPostcode" +
      "AboutYouWantsEmailContact" + "AboutYouMail" +
      "AboutYouNationalityAndResidencyActualNationality" +
      "OtherMoneyOtherEEAGuardQuestion" +
      "AboutYourPartnerTitle"       + "AboutYourPartnerFirstName" + "AboutYourPartnerFirstName" + "AboutYourPartnerSurname" +
      dateConversion("AboutYourPartnerDateofBirth") +
      "AboutTheCareYouProvideWhatTheirRelationshipToYou" +
      "AboutTheCareYouProvideTitlePersonCareFor"       + "AboutTheCareYouProvideFirstNamePersonCareFor" + "AboutTheCareYouProvideMiddleNamePersonCareFor" + "AboutTheCareYouProvideSurnamePersonCareFor" +
      dateConversion("AboutTheCareYouProvideDateofBirthPersonYouCareFor") +
      addressConversion("AboutTheCareYouProvideAddressPersonCareFor") + "AboutTheCareYouProvidePostcodePersonCareFor" +
      "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek" +
      "AboutTheCareYouProvideOtherCarer" +
      "AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1" +
      "EducationHaveYouBeenOnACourseOfEducation" +
      "EducationCourseTitle" +
      "EducationNameofSchool" +
      "EducationNameOfMainTeacherOrTutor" +
      "EducationPhoneNumber" +
      dateConversion("EducationWhenDidYouStartTheCourse") +
      dateConversion("EducationWhenDoYouExpectTheCourseToEnd") +
      "EmploymentHaveYouBeenEmployedAtAnyTime_0" +
      "EmploymentHaveYouBeenSelfEmployedAtAnyTime" +
      "ThirdPartyNameAndOrganisation"
  }
}


