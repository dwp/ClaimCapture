package models.claim

import play.api.cache.Cache
import play.api.mvc.{Action, Result, AnyContent, Request}
import play.api.http.HeaderNames._
import models.CreationTimeStamp

case class Claim(sections: Map[String, Section] = Map()) extends CreationTimeStamp {
  def section(sectionId: String): Option[Section] = {
    sections.get(sectionId)
  }

  def completedFormsForSection(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.forms.filter(form => form.section == sectionID)
    case _ => Nil
  }

  def update(form: Form): Claim = {
    val section = sections.get(form.section) match {
      case None => Section(form.section, List(form))
      case Some(s) => Section(form.section, s.forms.takeWhile(_.id != form.id) :+ form)
    }

    Claim(sections.updated(section.id, section))
  }

}

trait CachedClaim {

  import play.api.Play.current
  import scala.language.implicitConversions

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: (Claim, Result)) = Right(claimingResult)

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Claim()
      Cache.set(key, claim, expiration)

      f(claim)(request).withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-store")
    }
  }

  def claiming(f: => Claim => Request[AnyContent] => Either[Result, (Claim, Result)]) = Action {
    request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Cache.getOrElse(key, expiration)(Claim())

      f(claim)(request) match {
        case Left(r: Result) => r.withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-store")
        case Right((c: Claim, r: Result)) => {
          Cache.set(key, c, expiration)
          r.withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-store")
        }
      }
    }
  }
}