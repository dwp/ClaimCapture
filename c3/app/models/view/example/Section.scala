package models.view.example

object Section {

  def simple():GenericSection = {
    new GenericSection("simpleSection", Array (
      new GenericQuestionGroup("questionGroupOne", SingleStringInputForm()),
      new GenericQuestionGroup("questionGroupTwo", SingleStringInputForm()),
      new GenericQuestionGroup("questionGroupThree", SingleStringInputForm())
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
