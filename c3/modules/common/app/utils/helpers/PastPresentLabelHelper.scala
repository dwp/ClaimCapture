package utils.helpers

import models.domain._
import controllers.Mappings._

case class PastPresentLabelHelper(implicit claim: Claim)

object PastPresentLabelHelper {

  val didYou = "Did you"
  val doYou = "Do you"
  val wereYou = "were you"
  val areYou = "are you"

  def isWasIfSelfEmployed(implicit claim: Claim) = isSelfEmployed(claim) match {
    case true => "is"
    case false => "was"
  }

  def didYouDoYouIfSelfEmployed(implicit claim: Claim) = isSelfEmployed(claim) match {
    case true => "Do you"
    case false => "Did you"
  }

  private def isSelfEmployed(claim: Claim) = claim.questionGroup(AboutSelfEmployment) match {
    case Some(a: AboutSelfEmployment) => a.areYouSelfEmployedNow == "yes"
    case _ => false
  }

  def pastPresentLabelForEmployment(implicit claim: Claim, pastLabel: String, presentLabel: String, jobID: String) = {
    isTheJobFinished(claim, jobID) match {
      case true => presentLabel
      case false => pastLabel
    }
  }

  private def isTheJobFinished(claim: Claim, jobID: String) = theJobs(claim).questionGroup(jobID, JobDetails) match {
    case Some(j: JobDetails) => j.finishedThisJob == no
    case _ => false
  }

  def theJobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

}