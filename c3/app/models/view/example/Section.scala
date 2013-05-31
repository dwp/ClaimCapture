package models.view.example

object Section {

  def sectionOne():GenericSection = {
    new GenericSection("sectionOne", Array (
      new GenericQuestionGroup("s1.questionGroupOne", SingleStringInputForm()),
      new GenericQuestionGroup("s1.questionGroupTwo", SingleStringInputForm()),
      new GenericQuestionGroup("s1.questionGroupThree", SingleStringInputForm())
    )
    )
  }

  def sectionTwo():GenericSection = {
    new GenericSection("sectionTwo", Array (
      new GenericQuestionGroup("s2.questionGroupOne", SingleStringInputForm()),
      new GenericQuestionGroup("s2.questionGroupTwo", SingleStringInputForm()),
      new GenericQuestionGroup("s2.questionGroupThree", SingleStringInputForm())
    )
    )
  }

//  def eligibility():GenericSection = {
//      new GenericSection("eligibility", Array (
//          new GenericQuestionGroup("benefits", BenefitsForm() ),
//          new GenericQuestionGroup("hoursCaring", HoursCaringForm() )
//
//        )
//     )
//  }
//
//  def aboutYou():GenericSection = {
//    new GenericSection("aboutYou", Array (
//      new GenericQuestionGroup("details", DetailsForm() ),
//      new GenericQuestionGroup("contactDetails", ContactDetailsForm() ),
//      new GenericQuestionGroup("claimStartDate", ClaimStartDateForm())
//    )
//    )
//  }


}
