package models.view

import play.api.mvc.Call

trait Routing {
  val call: Call
}