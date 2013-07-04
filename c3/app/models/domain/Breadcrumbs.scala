package models.domain

case class Breadcrumbs(activeSection: String)
{
  private val allSections: Seq[String] = Seq(CarersAllowance.id, AboutYou.id, YourPartner.id, CareYouProvide.id) 
  
  val completedSections: Seq[String] = {
    allSections.takeWhile(_ < activeSection)
  }

  val futureSections: Seq[String] = {
    allSections.dropWhile(_ <= activeSection)
  }
}