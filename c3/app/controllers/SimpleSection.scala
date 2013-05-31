package controllers

import play.api.mvc._
import models.view.example.{GenericQuestionGroup, ExampleClaim, SingleStringInputForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import play.api.cache.Cache
import play.api.Play.current

object SimpleSection extends Controller {


  val form =
    Form(
      mapping(
        "answer" -> text
      )(SingleStringInputForm.apply)(SingleStringInputForm.unapply)
    )


  def presenter() = Action {
    request => request.session.get("connected").map { key =>

    val claim =  loadFromCache(key).get
    val sectionOption = claim.getFirstIncompleteSection()
    val answeredQuestionGroups = sectionOption.get.getAnsweredQuestionGroups()
    val nextQuestionGroup = sectionOption.get.getNextUnansweredQuestionGroup.getOrElse(answeredQuestionGroups.last)
    Ok(views.html.simple(answeredQuestionGroups, nextQuestionGroup, form))

    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Redirect(routes.SimpleSection.presenter()).withSession("connected" -> key)
    }


  }

  def command = Action { implicit request =>

    request.session.get("connected").map { key =>

    val claim =  loadFromCache(key).get
    val sectionOption = claim.getFirstIncompleteSection()
    val answeredQuestionGroups = sectionOption.get.getAnsweredQuestionGroups()
    val nextQuestionGroup:GenericQuestionGroup = sectionOption.get.getNextUnansweredQuestionGroup.getOrElse(answeredQuestionGroups.last)

    form.bindFromRequest.fold(
      errors => BadRequest(views.html.simple(answeredQuestionGroups, nextQuestionGroup, errors)),
      singleStringInputForm =>
        {
        nextQuestionGroup.updateForm(singleStringInputForm)
        nextQuestionGroup.answered = true
        updateCache(key, claim)
        Redirect(routes.SimpleSection.presenter())
        }
    )
    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Redirect(routes.SimpleSection.presenter()).withSession("connected" -> key)
    }

  }

  private def loadFromCache(key:String): Option[ExampleClaim] = {
    Logger.debug("loadFromCache: " + key )
    var claimOption:Option[ExampleClaim] = Cache.getAs[ExampleClaim](key)

    if(claimOption.isEmpty) {
      val claim =  ExampleClaim()
      Cache.set(key, claim)
      claimOption = Option(claim)
    }
    claimOption
  }


  private def updateCache(key:String, model:ExampleClaim) = {
    Logger.debug("updateCache: " + key + " " + model )
    Cache.set(key, model)
  }


}
