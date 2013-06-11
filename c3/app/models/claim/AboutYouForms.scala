package models.claim

import controllers.routes

case class YourDetailsForm(title: String, firstName: String, middleName: Option[String], surname: String,
                           otherNames: Option[String], nationalInsuranceNumber: Option[String], nationality: String,
                           dateOfBirth: String, maritalStatus: String, alwaysLivedUK: Boolean) extends Form {
  val id = "s2.g1"

  val url = routes.AboutYou.yourDetails()

  def approved: Boolean = true
}
