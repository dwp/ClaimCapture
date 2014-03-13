package controllers

import play.api.mvc.{Controller, Action}
import com.github.rjeschke.txtmark.Processor
import play.api.templates.Html
import views.html.claimNotes.markdownWrapper
import models.view.CachedClaim


object ClaimNotes extends Controller with CachedClaim {

  def notes() = Action {

    val t = views.html.claimNotes.main()
    val string = t.body

    val html = Processor.process(string)

    Ok(markdownWrapper(Html(html)))
  }

}
