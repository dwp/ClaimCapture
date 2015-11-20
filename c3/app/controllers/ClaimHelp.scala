package controllers

import play.api.Play._
import play.api.mvc.{Result, Results, Controller}
import com.github.rjeschke.txtmark.Processor
import play.twirl.api.Html
import models.view.{CachedChangeOfCircs, Navigable, CachedClaim}
import play.api.i18n._

object Help {
  def buildResponse(t: Html): Result = {
    val string = t.body
    Processor.process(string)
    Results.Ok(t)
  }
}

object ClaimHelp extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def notes() = claiming {implicit claim => implicit request => implicit lang =>
    Help.buildResponse(views.html.claimHelp.claimHelpNotes(request2lang))
  }

}
//
//object CofcHelp @Inject() (val mMessage: MMessages) extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
//
//  def notes() = claiming {implicit claim =>  implicit request =>  lang =>
//    Help.buildResponse(views.html.claimHelp.claimHelpNotes(lang))
//  }
//
//}
