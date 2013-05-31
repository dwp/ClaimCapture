package controllers

import play.api.mvc._
import models.view.example.{ExampleClaim, SingleStringInputForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import play.api.cache.Cache
import play.api.Play.current

object CommandController extends Controller {


  val form =
    Form(
      mapping(
        "answer" -> text
      )(SingleStringInputForm.apply)(SingleStringInputForm.unapply)
    )


  def command = Action { implicit request =>

    request.session.get("connected").map { key =>

    val claim =  loadFromCache(key).get
    val sectionOption = claim.getNextIncompleteSection()
    val answeredQuestionGroups = sectionOption.get.getAnsweredQuestionGroups()
    val nextQuestionGroup = sectionOption.get.getNextUnansweredQuestionGroup.get

    form.bindFromRequest.fold(
      errors => BadRequest(views.html.simple(answeredQuestionGroups, nextQuestionGroup, errors)),
      singleStringInputForm =>
        {
        nextQuestionGroup.updateForm(singleStringInputForm)
        nextQuestionGroup.answered = true
        updateCache(key, claim)
        Redirect(routes.SectionController.presenter())
        }
    )

    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Redirect(routes.SectionController.presenter()).withSession("connected" -> key)
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
