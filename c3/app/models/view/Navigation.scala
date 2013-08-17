package models.view

import play.api.mvc.{Request, Result, AnyContent}
import models.domain.Claim

trait Navigable {
  this: CachedClaim =>

  def track(f: => Claim => Result)(implicit claim: Claim, request: Request[AnyContent]) = {
    val updatedNavigation = claim.navigation.track(request.uri)
    val updatedClaim = claim.copy(claim.sections)(updatedNavigation)

    updatedClaim -> f(updatedClaim)
  }
}

case class Navigation(routes: List[String] = List()) {
  def track(route: String): Navigation = copy(routes.takeWhile(_ != route) :+ route)

  def current: String = if (routes.isEmpty) "" else routes.last

  def previous: String = if (routes.size > 1) routes.dropRight(1).last else if (routes.size == 1) current else ""
}

/*
for { routes <- play.api.Play.current.routes.toList
      (method, pattern, call) <- routes.documentation
} yield {
  println(call)
}
*/