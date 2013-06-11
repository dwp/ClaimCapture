package models.claim

case class YourDetailsForm(val firstName: String) extends Form {
  val id = "s2.g1"

  def approved: Boolean = true
}
