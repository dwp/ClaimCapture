package controllers

import play.api.mvc.{AnyContent, Action}

trait Presenter {

  def present: Action[AnyContent]

}
