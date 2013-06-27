package controllers

import play.api.mvc.{Controller, Call}

trait Routing {
  val route: (String, Call)
}

object Routing {
  import scala.language.implicitConversions

  implicit def controllerToRouting(c: Controller) = c.asInstanceOf[Routing].route
}