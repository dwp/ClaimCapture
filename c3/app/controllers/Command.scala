package controllers

import play.api.mvc.{AnyContent, Action}

trait Command {

  def command: Action[AnyContent]

}
