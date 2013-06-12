package models.claim

import controllers.routes

object AboutYou {
  val id = "s2"
}

case class YourDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                           otherNames: Option[String], nationalInsuranceNumber: Option[String], nationality: String,
                           dateOfBirth: String, maritalStatus: String, alwaysLivedUK: Boolean) extends Form {
  val id = YourDetails.id

  val url = routes.AboutYou.yourDetails()
}

object YourDetails {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: String, postcode: String, phoneNumber: Option[String], mobileNumber: Option[String]) extends Form{
  val id = ContactDetails.id

  val url = routes.AboutYou.contactDetails()
}

object ContactDetails {
  val id = s"${AboutYou.id}.g2"
}