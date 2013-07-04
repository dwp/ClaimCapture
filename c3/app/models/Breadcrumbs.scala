package models

import models.domain._

case class Breadcrumbs(activeSection: String)
{
  private val allSections: Seq[String] = Seq(CarersAllowance.id, AboutYou.id, CareYouProvide.id) 
  
  def completedSections: Seq[String] = Seq("A", "B")

  def futureSections: Seq[String] = Seq("C", "D")
}