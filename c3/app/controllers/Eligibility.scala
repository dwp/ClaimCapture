package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object Eligibility extends Controller {

  case class User(name: String, age: Int)



}
