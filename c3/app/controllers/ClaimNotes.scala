package controllers

import play.api.mvc.{Result, Action, Controller}
import com.github.rjeschke.txtmark.Processor
import play.twirl.api.Html
import views.html.claimNotes.markdownWrapper
import play.api.i18n.Lang


object ClaimNotes extends Controller {

  def internalNotes(languageCode:String) = Action {
      implicit var lang = Lang(languageCode)
      val t = views.html.claimNotes.main()
      buildResponse(t)
  }

//  def notes() = Action {
//    val t = views.html.claimNotes.main()
//    buildResponse(t)
//  }

  private def buildResponse(t: Html): Result = {
    val string = t.body
    val html = Processor.process(string)
    Ok(markdownWrapper(Html(html)))
  }
}
