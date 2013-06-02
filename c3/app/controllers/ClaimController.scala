package controllers

import play.api.mvc._
import models.view.example.{Claim, SingleStringInputForm}
import play.api.data.Form
import play.api.data.Forms._
import utils.CacheUtil
import scala.Predef._
import models.view.example.SingleStringInputForm

object ClaimController extends Controller {

  val form =
    Form(
      mapping(
        "answer" -> nonEmptyText
      )(SingleStringInputForm.apply)(SingleStringInputForm.unapply)
    )

  def presenter(sectionId: String) = Action {

    request => request.session.get("connected").map {
      key =>

        println("present: " + sectionId)

        val claim = CacheUtil.loadFromCache(key).get
        val sectionOption = claim.getSectionWithId(sectionId)

        if (sectionOption.isDefined) {
          sectionOption.get.name match {
            case "sectionOne" => Ok(views.html.sectionOne(sectionOption.get, form))
            case "sectionTwo" => Ok(views.html.sectionTwo(sectionOption.get, form))
          }
        } else {
          NotFound
        }
    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Redirect(routes.ClaimController.presenter(sectionId)).withSession("connected" -> key)
    }
  }

  def command(sectionId: String) = Action {
    implicit request =>

      request.session.get("connected").map {
        key =>

          val claim = CacheUtil.loadFromCache(key).get
          val sectionOption = claim.getSectionWithId(sectionId)

          var sectionToNavigateTo = "";

          if (sectionOption.isDefined) {
            val section = sectionOption.get
            sectionToNavigateTo = section.name
            val nextQuestionGroupOption = section.getNextUnansweredQuestionGroup

            if (nextQuestionGroupOption.isDefined) {
              val nextQuestionGroup = nextQuestionGroupOption.get
              form.bindFromRequest.fold(
                errors => BadRequest(views.html.sectionOne(section, errors)),
                singleStringInputForm => {
                  nextQuestionGroup.updateForm(singleStringInputForm)
                  nextQuestionGroup.answered = true
                  CacheUtil.updateCache(key, claim)

                }

              )

              if (section.isComplete) {
                val nextSectionOption = claim.getNextIncompleteSection
                if (nextSectionOption.isDefined) {
                  val nextSection = nextSectionOption.get
                  sectionToNavigateTo = nextSection.name
                }
              }
            }
          }

          Redirect(routes.ClaimController.presenter(sectionToNavigateTo))

      }.getOrElse {
        val key = java.util.UUID.randomUUID().toString
        Ok("Session key:" + key)
      }

  }
}
