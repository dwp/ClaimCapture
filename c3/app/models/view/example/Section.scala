package models.view.example

object Section {

  def eligibility():GenericSection = {
      new GenericSection("eligibility", Array (
          new GenericQuestionGroup("benefits", BenefitsForm() ),
          new GenericQuestionGroup("hoursCaring", HoursCaringForm() )

        )
     )
  }

  def aboutYou():GenericSection = {
    new GenericSection("aboutYou", Array (
      new GenericQuestionGroup("details", DetailsForm() ),
      new GenericQuestionGroup("contactDetails", ContactDetailsForm() ),
      new GenericQuestionGroup("claimStartDate", ClaimStartDateForm())
    )
    )
  }


}
