package models.domain

case class Breadcrumbs(activeSection: String)
{
  private val allSections: Seq[String] = Seq(CarersAllowance.id, AboutYou.id, YourPartner.id, CareYouProvide.id) 
  
  def completedSections: Seq[String] = {
    allSections.takeWhile(_ < activeSection)
  }

  def futureSections: Seq[String] = {
    allSections.dropWhile(_ <= activeSection)
  }
}