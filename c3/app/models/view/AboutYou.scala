package models.view

class AboutYou extends Section {
  def name: String = "AboutYou"
  val questionGroups: Seq[QuestionGroup] = Seq(new Details, new ContactDetails, new ClaimStartDate)
}

class Details extends QuestionGroup {
  def name: String = "Details"
  val detailsForm = DetailsForm
}
case class DetailsForm(firstName : Option[String] = None, lastName: Option[String] = None)

class ContactDetails extends QuestionGroup {
  def name: String = "contactDetails"
  val contactDetailsForm = ContactDetailsForm()
}
case class ContactDetailsForm(postCode : Option[String] = None)

class ClaimStartDate extends QuestionGroup {
  def name: String = "contactDetails"
  val claimStartDateForm = ClaimStartDateForm()
}
case class ClaimStartDateForm(claimStart: Option[String] = None)
