package controllers

import play.api.mvc._

object Application extends Controller {
  def index = Action {
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }

  def timeout = Action {
    Ok(views.html.common.session_timeout())
  }

  def error = Action {
    Ok(views.html.common.error())
  }

  def previous = TODO
}



/*
  var action: Action[AnyContent] = _

  def present = blah {
    claiming { implicit claim => implicit request =>
      Ok(views.html.s2_about_you.g2_contactDetails(form.fill(ContactDetails), completedQuestionGroups(ContactDetails)))
    }
  }

  def submit = action

  def blah(a: => Action[AnyContent]): Action[AnyContent] = {

    action = a
    a
  }*/