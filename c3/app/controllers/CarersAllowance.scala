package controllers

import play.api.mvc._

object CarersAllowance extends Controller with Presenter with Command {

  def present = Action {
    Ok(views.html.carersAllowance())
  }

  def command = Action {
    Ok("Carer's Allowance - Command")
  }

}
