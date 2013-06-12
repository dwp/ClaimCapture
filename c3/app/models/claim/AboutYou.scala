package models.claim

import controllers.routes
import play.api.data.Form

object AboutYou {
  val id = "s2"
}

case class YourDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                           otherNames: Option[String], nationalInsuranceNumber: Option[String], nationality: String,
                           dateOfBirth: String, maritalStatus: String, alwaysLivedUK: String, action:String) extends Form with NavAction {
  val id = YourDetails.id

  val url = routes.AboutYou.yourDetails()

  val next = routes.AboutYou.contactDetails()

  val previous = routes.CarersAllowance.approve()
}

object YourDetails {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: String, postcode: String, phoneNumber: Option[String], mobileNumber: Option[String], action:String) extends Form with NavAction {
  val id = ContactDetails.id

  val url = routes.AboutYou.contactDetails()

  val next = routes.AboutYou.contactDetails()

  val previous = routes.AboutYou.yourDetails()

}

object ContactDetails {
  val id = s"${AboutYou.id}.g2"
}