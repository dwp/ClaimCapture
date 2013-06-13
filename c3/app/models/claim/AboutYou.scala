package models.claim

import controllers.routes
import models.DayMonthYear

object AboutYou {
  val id = "s2"
}

case class YourDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                       otherNames: Option[String], nationalInsuranceNumber: Option[String], nationality: String,
                       dateOfBirth: DayMonthYear, maritalStatus: String, alwaysLivedUK: String, action:Option[String]) extends Form {
  val id = YourDetails.id

  val next = routes.AboutYou.contactDetails()

  val previous = routes.CarersAllowance.approve()
}

object YourDetails {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: String, postcode: String, phoneNumber: Option[String], mobileNumber: Option[String], action:Option[String]) extends Form {
  val id = ContactDetails.id

  val next = routes.AboutYou.contactDetails()

  val previous = routes.AboutYou.yourDetails()

}

object ContactDetails {
  val id = s"${AboutYou.id}.g2"
}

case class ClaimDate(dateOfClaim: DayMonthYear,action: Option[String]) extends Form {
  val id = ClaimDate.id

  val next = routes.AboutYou.moreAboutYou()
  val previous = routes.AboutYou.contactDetails()
}

object ClaimDate {
  val id = s"${AboutYou.id}.g3"
}
