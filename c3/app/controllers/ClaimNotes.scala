package controllers

import app.ConfigProperties._
import play.api.mvc.{AnyContent, Request, Controller, Action}
import com.github.rjeschke.txtmark.Processor
import play.api.templates.Html
import views.html.claimNotes.markdownWrapper
import models.view.CachedClaim
import play.api.i18n.Lang


object ClaimNotes extends Controller with CachedClaim {

  def notes() = claiming {
    implicit claim => implicit request => implicit lang =>

    val t = views.html.claimNotes.main()
    val string = t.body

    val html = Processor.process(string)

    Ok(markdownWrapper(Html(html)))
  }

}
