package utils.helpers

import models.domain._
import controllers.mappings.Mappings._
import play.api.i18n.{MMessages, MessagesApi, Lang}
import play.api.Play.current

case class PastPresentLabelHelper(implicit claim: Claim, lang:Lang)

object PastPresentLabelHelper {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def labelForSelfEmployment(implicit claim: Claim, lang: Lang, labelKey: String) = {
    messagesApi(isSelfEmployed(claim) match {
      case true => labelKey + ".present"
      case false => labelKey + ".past"
    })
  }

  def valuesForSelfEmployment(implicit claim: Claim, lang: Lang, pastYes: String, pastNo: String, presentYes: String, presentNo: String) = {
    isSelfEmployed(claim) match {
      case true => 'values -> Seq("yes" -> messagesApi(presentYes), "no" -> messagesApi(presentNo))
      case false => 'values -> Seq("yes" -> messagesApi(pastYes), "no" -> messagesApi(pastNo))
    }
  }

  private def isSelfEmployed(claim: Claim) = claim.questionGroup(SelfEmploymentDates) match {
    case Some(a: SelfEmploymentDates) => a.stillSelfEmployed == "yes"
    case _ => false
  }

  def labelForEmployment(finishedThisJob:String, lang: Lang, labelKey: String) = {
    messagesApi(finishedThisJob == yes match {
      case true => labelKey + ".past"
      case false => labelKey + ".present"
    })
  }

  def labelForEmployment(implicit claim: Claim, lang: Lang, labelKey: String, jobID: String) = {
    messagesApi(isTheJobFinished(claim, jobID) match {
      case true => labelKey + ".present"
      case false => labelKey + ".past"
    })
  }

  def valuesForEmployment(implicit claim: Claim, lang: Lang, pastYes: String, pastNo: String, presentYes: String, presentNo: String, jobID: String) = {
    isTheJobFinished(claim, jobID) match {
      case true => 'values -> Seq("yes" -> messagesApi(presentYes), "no" -> messagesApi(presentNo))
      case false => 'values -> Seq("yes" -> messagesApi(pastYes), "no" -> messagesApi(pastNo))
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
