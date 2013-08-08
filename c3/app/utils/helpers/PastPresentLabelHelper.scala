package utils.helpers

import models.domain.{JobDetails, AboutSelfEmployment, Claim}


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

  def pastPresentLabelForEmployment (implicit claim: Claim, pastLabel:String, presentLabel:String) = {
     isTheJobFinished(claim) match {
       case true => presentLabel
       case false => pastLabel
     }
  }

  private def isTheJobFinished (claim:Claim) = {
    claim.questionGroup(JobDetails) match {
      case Some(s: JobDetails) => s.finishedThisJob == "no"
      case _ => false
    }
  }
}
