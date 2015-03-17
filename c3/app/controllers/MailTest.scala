package controllers

import play.api.mvc.{Action, Controller}
import services.EmailServices

/**
 * Created by valtechuk on 16/03/2015.
 */
object MailTest extends Controller{

  def send = Action{ request =>

    EmailServices.sendEmail to "ruben.diaz@valtech.co.uk"
    Ok("")
  }

}
