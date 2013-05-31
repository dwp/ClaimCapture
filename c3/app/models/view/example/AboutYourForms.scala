package models.view.example

case class DetailsForm(firstName : Option[String] = None, lastName: Option[String] = None)

case class ContactDetailsForm(postCode : Option[String] = None)

case class ClaimStartDateForm(claimStart: Option[String] = None)
