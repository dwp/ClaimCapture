package controllers

import play.api.mvc.{Controller, Action}
import com.github.rjeschke.txtmark.Processor
import play.api.templates.Html
import views.html.markdown.markdownWrapper


object Markdown extends Controller{

  def test() = Action{
    implicit request =>

    val t = views.html.markdown.main()
    val string = t.buffer.toString()

    val html = Processor.process(string)

    Ok(markdownWrapper(Html(html)))
  }

}
