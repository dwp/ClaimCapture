package controllers

import play.api.mvc.{SimpleResult, Action, Controller}
import com.github.rjeschke.txtmark.Processor
import play.api.templates.Html
import views.html.claimNotes.markdownWrapper
import play.api.i18n.Lang
import models.view.{Navigable, CachedClaim}


object ClaimHelp extends Controller with CachedClaim with Navigable {

  def notes() = claiming { implicit claim => implicit request => implicit lang =>
    val t = views.html.claimHelp.claimHelpNotes()
    buildResponse(t)
  }

  private def buildResponse(t: Html): SimpleResult = {
    val string = t.body
    val html = Processor.process(string)
    Ok(t)
  }
}
