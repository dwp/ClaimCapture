package controllers

import play.api.mvc._
import play.api.Logger

object SubmissionService extends Controller {
  def submit = Action {
    request =>
      request.body.asXml.map {
        xml =>
          println("SubmissionService.submit")
          Ok(xml)
      }.getOrElse {
        BadRequest("<p>Expecting Xml data</p>")
      }
  }
}