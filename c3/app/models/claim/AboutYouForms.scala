package models.claim

case class YourDetailsForm(title: String,firstName: String  ,middleName: Option[String]   ,surname: String,
                           otherSurnames:Option[String],nationalInsuranceNo:Option[String],nationality: String,
                           birthDate: String, maritalStatus:String, alwaysLivedUK:Boolean
                           ) extends Form {
  val id = "s2.g1"

  def approved: Boolean = true
}
