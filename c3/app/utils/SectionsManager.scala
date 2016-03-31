package utils

import models.domain._

object SectionsManager {

  //This list has to change for any section modification (add/remove/modify)
  private val claimSections = List(
    ThirdParty,
    YourClaimDate,
    AboutYou,
    YourPartner,
    CareYouProvide,
    Breaks,
    Education,
    YourIncome,
    OtherMoney,
    PayDetails,
    Information
  )

  private def filterByVisibility(claim:Claim):Seq[Section.Identifier] = {
    //We don't have to count in max number of sections those that are not visible
    claimSections.filter{section =>
      claim.sections.find(_.identifier == section) match {
        case Some(s) => s.visible
        case _ => true
      }
    }
  }

  def claimSectionsNum(implicit claim:Claim):Int = {
   filterByVisibility(claim).size
  }

  def currentSection(s:Section.Identifier)(implicit claim:Claim):Int = {
    filterByVisibility(claim).zipWithIndex.find(_._1 == s).getOrElse(AnyRef -> claimSections.indexOf(s))._2 + 1
  }
}
