package controllers

import play.api.mvc.{Results, SimpleResult, Action, Controller}
import com.github.rjeschke.txtmark.Processor
import play.api.templates.Html
import views.html.claimNotes.markdownWrapper
import play.api.i18n.Lang
import models.view.{CachedChangeOfCircs, Navigable, CachedClaim}

object Help {
  def buildResponse(t: Html): SimpleResult = {
    val string = t.body
    val html = Processor.process(string)
    Results.Ok(t)
  }
}

object ClaimHelp extends Controller with CachedClaim with Navigable {

  def notes() = claiming { implicit claim => implicit request => implicit lang =>
    Help.buildResponse(views.html.claimHelp.claimHelpNotes())
  }

}

object CofcHelp extends Controller with CachedChangeOfCircs with Navigable {

  def notes() = claiming { implicit claim => implicit request => implicit lang =>
    Help.buildResponse(views.html.claimHelp.claimHelpNotes())
  }

}
