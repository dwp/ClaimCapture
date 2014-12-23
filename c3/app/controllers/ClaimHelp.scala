package controllers

import play.api.mvc.{Result, Results, Controller}
import com.github.rjeschke.txtmark.Processor
import play.twirl.api.Html
import models.view.{CachedChangeOfCircs, Navigable, CachedClaim}

object Help {
  def buildResponse(t: Html): Result = {
    val string = t.body
    Processor.process(string)
    Results.Ok(t)
  }
}

object ClaimHelp extends Controller with CachedClaim with Navigable {

  def notes() = claiming {implicit claim =>  implicit request =>  lang =>
    Help.buildResponse(views.html.claimHelp.claimHelpNotes(lang))
  }

}

object CofcHelp extends Controller with CachedChangeOfCircs with Navigable {

  def notes() = claiming {implicit claim =>  implicit request =>  lang =>
    Help.buildResponse(views.html.claimHelp.claimHelpNotes(lang))
  }

}
