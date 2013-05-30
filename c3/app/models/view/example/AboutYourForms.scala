package models.view.example

case class DetailsForm(firstName : Option[String] = None, lastName: Option[String] = None) extends Form

case class ContactDetailsForm(postCode : Option[String] = None) extends Form

case class ClaimStartDateForm(claimStart: Option[String] = None)  extends Form
