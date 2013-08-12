package utils.helpers

import models.domain._
import controllers.Mappings._
import controllers.s7_employment.Employment._
import scala.Some


case class PastPresentLabelHelper(implicit claim: Claim)


object PastPresentLabelHelper {

  val didYou = "Did you"
  val doYou = "Do you"
  val wereYou = "were you"
  val areYou = "are you"


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

  private def isSelfEmployed(claim: Claim) = {
    claim.questionGroup(AboutSelfEmployment) match {
      case Some(s: AboutSelfEmployment) => s.areYouSelfEmployedNow == "yes"
      case _ => false
    }
  }

  def pastPresentLabelForEmployment(implicit claim: Claim, pastLabel: String, presentLabel: String, jobID: String) = {
    isTheJobFinished(claim, jobID) match {
      case true => presentLabel
      case false => pastLabel
    }
  }

  private def isTheJobFinished(claim: Claim, jobID: String) = {
      jobs(claim).questionGroup(jobID, JobDetails) match {
      case Some(s: JobDetails) => s.finishedThisJob == no
      case _ => false
    }
  }
}
