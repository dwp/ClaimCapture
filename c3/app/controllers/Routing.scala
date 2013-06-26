package controllers

import play.api.mvc.Call

trait Routing {
  val route: (String, Call)
}