package models

import play.api.cache.Cache
import play.api.mvc.{Action, Result, AnyContent, Request}

case class Claim(sections: Map[String, Section] = Map()) extends CreationTimeStamp {

  def section(sectionId: String): Option[Section] = {
    sections.get(sectionId)
  }

  /*def getNextIncompleteSection: Option[Section] = {
    sections.find(section => !section.isComplete)
  }

  def findSectionForClaim(sectionId: String, claim: Claim) = {
    claim.sections.find(section => section.id.equals(sectionId))
  }

  def findQuestionGroupForSection(sectionId: String, questionGroupId: String, claim: Claim) = {
    val sectionOption = findSectionForClaim(sectionId, claim)
    sectionOption.get.forms.find(questionGroup => questionGroup.id.equals(questionGroupId))
  }

  def findFormForQuestionGroup(sectionId: String, questionGroupId: String, claim: Claim) = {
    findQuestionGroupForSection(sectionId, questionGroupId, claim).get
  }*/

  def answeredFormsForSection(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.forms.filter(form => form.section == sectionID)
    case _ => Nil
  }

  def update(form: CarersAllowanceForm): Claim = {
    val section = sections.get(form.section) match {
      case None => Section(form.section, List(form))
      case Some(s) => Section(form.section, s.forms.filterNot(_.id == form.id) :+ form  )
    }

    Claim(sections.updated(section.id, section))
  }
}

trait CachedClaim {

  import play.api.Play.current

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Claim()
      Cache.set(key, claim, expiration)
      f(claim)(request).withSession("connected" -> key)
    }
  }

  def claiming(f: => Claim => Request[AnyContent] => Either[Result, (Claim, Result)]) = Action {
    request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Cache.getOrElse(key, expiration)(Claim())

      f(claim)(request) match {
        case Left(r: Result) => r.withSession("connected" -> key)
        case Right((c: Claim, r: Result)) => {
          Cache.set(key, c, expiration)
          r.withSession("connected" -> key)
        }
      }
    }
  }
}