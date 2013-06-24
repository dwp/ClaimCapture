package controllers

import play.api.mvc._

object SubmissionService extends Controller {

  def submit = Action {
    request =>
      request.body.asXml.map {
        xml =>
          Ok(xml)
      }.getOrElse {
        BadRequest("Expecting Xml data")
      }
  }
}