package utils.helpers

import models.domain._
import controllers.Mappings._
import play.api.i18n.{MMessages => Messages, Lang}

case class PastPresentLabelHelper(implicit claim: Claim, lang:Lang)

object PastPresentLabelHelper {

  def is (implicit lang:Lang) = {
    Messages("is")
  }

  def was (implicit lang:Lang) = {
    Messages("was")
  }

  def areYou (implicit lang:Lang) = {
    Messages("areYou")
  }

  def wereYou (implicit lang:Lang) = {
    Messages("wereYou")
  }

  def doYou (implicit lang:Lang) = {
    Messages("doYou")
  }

  def didYou (implicit lang:Lang) = {
    Messages("didYou")
  }

  def didYouDoYouIfSelfEmployed(implicit claim: Claim,lang:Lang) = isSelfEmployed(claim) match {
    case true => doYou
    case false => didYou
  }

  def labelForSelfEmployment(implicit claim: Claim, labelKey: String) = {
    Messages(isSelfEmployed(claim) match {
      case true => labelKey + ".present"
      case false => labelKey + ".past"
    })
  }

  private def isSelfEmployed(claim: Claim) = claim.questionGroup(AboutSelfEmployment) match {
    case Some(a: AboutSelfEmployment) => a.areYouSelfEmployedNow == "yes"
    case _ => false
  }

  def labelForEmployment(implicit claim: Claim, labelKey: String, jobID: String) = {
    Messages(isTheJobFinished(claim, jobID) match {
      case true => labelKey + ".present"
      case false => labelKey + ".past"
    })
  }

  private def isTheJobFinished(claim: Claim, jobID: String) = theJobs(claim).questionGroup(jobID, JobDetails) match {
    case Some(j: JobDetails) => j.finishedThisJob == no
    case _ => false
  }

  private def theJobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }
}