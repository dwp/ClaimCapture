package models.domain

case class ProgressBar(currentSectionId: String) {
  private case class Crumb(val id: String, val name: String)

  private val allSections: Seq[Crumb] = Seq(
    Crumb(CarersAllowance.id, "CarersAllowance"),
    Crumb(AboutYou.id, "AboutYou"),
    Crumb(YourPartner.id, "YourPartner"),
    Crumb(CareYouProvide.id, "CareYouProvide"))

  val completedSections: Seq[String] = {
    allSections.takeWhile(_.id < currentSectionId).map(_.name)
  }

  val futureSections: Seq[String] = {
    allSections.dropWhile(_.id <= currentSectionId).map(_.name)
  }
  
  val activeSection = allSections.find(_.id ==  currentSectionId).get.name
}