package utils.helpers

import models.domain._
import controllers.Mappings._
import play.api.i18n.{MMessages => Messages, Lang}

case class PastPresentLabelHelper(implicit claim: Claim, lang:Lang)

object PastPresentLabelHelper {
  def labelForSelfEmployment(implicit claim: Claim, lang: Lang, labelKey: String) = {
    Messages(isSelfEmployed(claim) match {
      case true => labelKey + ".present"
      case false => labelKey + ".past"
    })
  }

  def valuesForSelfEmployment(implicit claim: Claim, lang: Lang, pastYes: String, pastNo: String, presentYes: String, presentNo: String) = {
    isSelfEmployed(claim) match {
      case true => 'values -> Seq("yes" -> Messages(presentYes), "no" -> Messages(presentNo))
      case false => 'values -> Seq("yes" -> Messages(pastYes), "no" -> Messages(pastNo))
    }
  }

  private def isSelfEmployed(claim: Claim) = claim.questionGroup(AboutSelfEmployment) match {
    case Some(a: AboutSelfEmployment) => a.areYouSelfEmployedNow == "yes"
    case _ => false
  }

  def labelForEmployment(implicit claim: Claim, lang: Lang, labelKey: String, jobID: String) = {
    Messages(isTheJobFinished(claim, jobID) match {
      case true => labelKey + ".present"
      case false => labelKey + ".past"
    })
  }

  def valuesForEmployment(implicit claim: Claim, lang: Lang, pastYes: String, pastNo: String, presentYes: String, presentNo: String, jobID: String) = {
    isTheJobFinished(claim, jobID) match {
      case true => 'values -> Seq("yes" -> Messages(presentYes), "no" -> Messages(presentNo))
      case false => 'values -> Seq("yes" -> Messages(pastYes), "no" -> Messages(pastNo))
    }
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