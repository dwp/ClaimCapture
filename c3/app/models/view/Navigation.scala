package models.view

import play.api.mvc.{AnyContent, Action}

case class Navigation(routes: List[String] = List()) {
  def track(route: String): Navigation = Navigation(routes :+ route)

  def backup: Navigation = Navigation(if (routes.size > 1) routes.dropRight(1) else routes)

  def current: String = if (routes.isEmpty) "" else routes.last
}

object Navigation extends CachedClaim {
  def apply() = new Navigation()

  def track(action: => Action[AnyContent]) = claiming { implicit claim => implicit request =>
    val updatedNavigation = claim.navigation.track(request.uri)
    val updatedClaim = claim.copy(claim.sections)(updatedNavigation)

    updatedClaim -> action(request)
  }
}

/*
for { routes <- play.api.Play.current.routes.toList
      (method, pattern, call) <- routes.documentation
} yield {
  println(call)
}
*/