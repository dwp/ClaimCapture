package controllers

import play.api.mvc._
import models.view.example.{ExampleClaim, SingleStringInputForm}
import play.api.data.Form
import play.api.data.Forms._
import utils.CacheUtil
import scala.Predef._
import models.view.example.SingleStringInputForm

object CommandController extends Controller {

  val form =
    Form(
      mapping(
        "answer" -> text
      )(SingleStringInputForm.apply)(SingleStringInputForm.unapply)
    )

  def command(sectionId:String) = Action { implicit request =>

    println("command: " + sectionId)

    request.session.get("connected").map { key =>

      println("KEY: " + key)

      val claim =  CacheUtil.loadFromCache(key).get
      val sectionOption = claim.getSectionWithId(sectionId)
      var sectionToNavigateTo = "";

      if (sectionOption.isDefined) {
        val section = sectionOption.get

        val nextQuestionGroupOption = section.getNextUnansweredQuestionGroup

        if(nextQuestionGroupOption.isDefined) {
            val nextQuestionGroup = nextQuestionGroupOption.get
            form.bindFromRequest.fold(
              errors => BadRequest(views.html.sectionOne(section, errors)),
              singleStringInputForm =>
              {
                nextQuestionGroup.updateForm(singleStringInputForm)
                nextQuestionGroup.answered = true
                CacheUtil.updateCache(key, claim)

              }
            )

          sectionToNavigateTo = section.name
          if(section.isComplete) {
            val nextSectionOption = claim.getNextIncompleteSection()
            val nextSection = nextSectionOption.get

            Redirect(routes.PresenterController.present(nextSection.name))
          }
        }
      }
      Redirect(routes.PresenterController.present(sectionId))

    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Ok("Session key:" + key)
    }

  }




}
