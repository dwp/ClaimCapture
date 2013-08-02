package utils.helpers

import models.domain.{AboutSelfEmployment, Claim}


case class PastPresentLabelHelper(implicit claim: Claim)


object PastPresentLabelHelper {

  def isWasIfSelfEmployed(implicit claim: Claim) = {
    isSelfEmployed(claim) match {
      case true => "is"
      case false => "was"
    }
  }

  def didYouDoYouIfSelfEmployed(implicit claim: Claim) = {
    isSelfEmployed(claim) match {
      case true => "Do you"
      case false => "Did you"
    }
  }

  private def isSelfEmployed (claim:Claim) = {
    claim.questionGroup(AboutSelfEmployment) match {
      case Some(s: AboutSelfEmployment) => s.areYouSelfEmployedNow == "yes"
      case _ => false
    }
  }
}
