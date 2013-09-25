package models.view

import play.api.mvc.{Request, Result, AnyContent}
import models.domain.DigitalForm
import scala.reflect.ClassTag

trait Navigable {
  this: CachedDigitalForm =>

  def track[T](t: T)(f: (DigitalForm) => Result)(implicit claim: DigitalForm, request: Request[AnyContent], classTag: ClassTag[T]): FormResult = {
    val updatedNavigation = claim.navigation.track(t)(request.uri)
    val updatedClaim = claim.copyForm(claim.sections)(updatedNavigation)

    updatedClaim -> f(updatedClaim)
  }
}

case class Navigation(routes: List[Route[_]] = List()) {
  def track[T](t: T)(route: String)(implicit classTag: ClassTag[T]): Navigation = copy(routes.takeWhile(_.uri != route) :+ Route[T](route))

  def current: Route[_] = if (routes.isEmpty) Route("") else routes.last

  def previous: Route[_] = if (routes.size > 1) routes.dropRight(1).last else if (routes.size == 1) current else Route("")

  def apply[T](t: T)(implicit classTag: ClassTag[T]): Option[Route[_]] = routes.find(_.classTag.runtimeClass == t.getClass)
}

case class Route[T](uri: String)(implicit val classTag: ClassTag[T]) {
  override def toString = uri
}



/*
for { routes <- play.api.Play.current.routes.toList
      (method, pattern, call) <- routes.documentation
} yield {
  println(call)
}
*/