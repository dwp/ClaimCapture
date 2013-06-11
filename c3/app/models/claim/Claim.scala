package models.claim

import play.api.cache.Cache
import play.api.mvc.{Action, Result, AnyContent, Request}
import models.CreationTimeStamp
import utils.ClaimUtils

case class Claim(sections: Map[String, Section] = Map()) extends CreationTimeStamp {
  def section(sectionId: String): Option[Section] = {
    sections.get(sectionId)
  }

  def form(formId: String): Option[Form] = {
    val sectionId = ClaimUtils.sectionId(formId)
    section(sectionId) match {
      case Some(s: Section) => s.form(formId)
      case _ => None
    }
  }

  def completedFormsForSection(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.forms
    case _ => Nil
  }

  def update(form: Form): Claim = {
    def update(form: Form, forms: List[Form]) = {
      val updated = forms.map(f => if (f.id == form.id) form else f)
      if (updated.contains(form)) updated else updated :+ form
    }
    val sectionId = ClaimUtils.sectionId(form.id)
    val section = sections.get(sectionId) match {
      case None => Section(sectionId, List(form))
      case Some(s) => Section(sectionId, update(form, s.forms))
    }

    Claim(sections.updated(section.id, section))
  }
}

trait CachedClaim {

  import play.api.Play.current
  import scala.language.implicitConversions
  import play.api.http.HeaderNames._

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: (Claim, Result)) = Right(claimingResult)

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Claim()
      Cache.set(key, claim, expiration)

      f(claim)(request).withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-cache, no-store")
    }
  }

  def claiming(f: => Claim => Request[AnyContent] => Either[Result, (Claim, Result)]) = Action {
    request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Cache.getOrElse(key, expiration)(Claim())

      f(claim)(request) match {
        case Left(r: Result) => r.withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-cache, no-store")

        case Right((c: Claim, r: Result)) => {
          Cache.set(key, c, expiration)
          r.withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-cache, no-store")
        }
      }
    }
  }
}