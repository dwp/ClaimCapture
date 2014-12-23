package controllers.submission

import play.api.mvc.{AnyContent, Action}

trait ClaimSubmittable {

  def submit: Action[AnyContent]

}
