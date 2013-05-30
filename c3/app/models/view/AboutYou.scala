package models.view


class AboutYou(val questionGroups: Seq[QuestionGroup]) extends Section {
  def name: String = "AboutYou"
}

object AboutYou {
  def apply() = {
    new AboutYou(Seq(Details(), ContactDetails(), ClaimStartDate()))
  }
}

class Details(val form : CarersForm) extends QuestionGroup {
  def name: String = "Details"
  val detailsForm = DetailsForm
}
object Details {
  def apply() = {
    new Details(DetailsForm())
  }
}
case class DetailsForm(firstName : Option[String] = None, lastName: Option[String] = None) extends CarersForm


class ContactDetails(val form : CarersForm) extends QuestionGroup {
  def name: String = "contactDetails"
}
object ContactDetails {
  def apply() = {
    new ContactDetails(ContactDetailsForm())
  }
}
case class ContactDetailsForm(postCode : Option[String] = None) extends CarersForm


class ClaimStartDate(val form : CarersForm) extends QuestionGroup {
  def name: String = "claimStartDate"
  val claimStartDateForm = ClaimStartDateForm()
}
object ClaimStartDate {
  def apply() = {
    new ClaimStartDate(ClaimStartDateForm())
  }
}
case class ClaimStartDateForm(claimStart: Option[String] = None) extends CarersForm
