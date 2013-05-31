package controllers

import play.api.mvc._
import models.view.example.{ExampleClaim, SingleStringInputForm}
import play.api.data.Form
import play.api.data.Forms._
import utils.CacheUtil

object CommandController extends Controller {

  val form =
    Form(
      mapping(
        "answer" -> text
      )(SingleStringInputForm.apply)(SingleStringInputForm.unapply)
    )


  def command(sessionId:String) = Action { implicit request =>

      request.session.get("connected").map { key =>

        val claim =  CacheUtil.loadFromCache(key).get
        val section = claim.sections(0)
        val answeredQuestionGroups = section.getAnsweredQuestionGroups()
        val nextQuestionGroupOption = section.getNextUnansweredQuestionGroup

        if(nextQuestionGroupOption.isDefined) {
          val nextQuestionGroup = nextQuestionGroupOption.get
          form.bindFromRequest.fold(
            errors => BadRequest(views.html.sectionOne(section, errors)),
//            errors => BadRequest(views.html.sectionOne(answeredQuestionGroups, nextQuestionGroup, errors)),
            singleStringInputForm =>
            {
              nextQuestionGroup.updateForm(singleStringInputForm)
              nextQuestionGroup.answered = true
              CacheUtil.updateCache(key, claim)
              Redirect(routes.PresenterController.present(section.name))
            }
          )
        }
        else {
         Ok("No Questions")
        }

      }.getOrElse {
        val key = java.util.UUID.randomUUID().toString
        Redirect(routes.PresenterController.present(sessionId)).withSession("connected" -> key)
      }
  }


//    def sectionOne = Action { implicit request =>
//
//    request.session.get("connected").map { key =>
//
//    val claim =  CacheUtil.loadFromCache(key).get
//    val section = claim.sections(0)
//    val answeredQuestionGroups = section.getAnsweredQuestionGroups()
//    val nextQuestionGroupOption = section.getNextUnansweredQuestionGroup
//
//    if(nextQuestionGroupOption.isDefined) {
//      val nextQuestionGroup = nextQuestionGroupOption.get
//      form.bindFromRequest.fold(
//        errors => BadRequest(views.html.sectionOne(answeredQuestionGroups, nextQuestionGroup, errors)),
//        singleStringInputForm =>
//        {
//          nextQuestionGroup.updateForm(singleStringInputForm)
//          nextQuestionGroup.answered = true
//          CacheUtil.updateCache(key, claim)
//          Redirect(routes.PresenterController.present())
//        }
//      )
//    }
//    else {
//      Redirect(routes.CommandController.sectionTwo())
//    }
//
//
//
//    }.getOrElse {
//      val key = java.util.UUID.randomUUID().toString
//      Redirect(routes.PresenterController.present()).withSession("connected" -> key)
//    }
//
//  }
//
//  def sectionTwo = Action { implicit request =>
//
//    request.session.get("connected").map { key =>
//
//      val claim =  CacheUtil.loadFromCache(key).get
//      val section = claim.sections(1)
//      val answeredQuestionGroups = section.getAnsweredQuestionGroups()
//      val nextQuestionGroup = section.getNextUnansweredQuestionGroup.get
//
//      form.bindFromRequest.fold(
//        errors => BadRequest(views.html.sectionTwo(answeredQuestionGroups, nextQuestionGroup, errors)),
//        singleStringInputForm =>
//        {
//          nextQuestionGroup.updateForm(singleStringInputForm)
//          nextQuestionGroup.answered = true
//          CacheUtil.updateCache(key, claim)
//          Redirect(routes.PresenterController.present())
//        }
//      )
//
//    }.getOrElse {
//      val key = java.util.UUID.randomUUID().toString
//      Redirect(routes.PresenterController.present()).withSession("connected" -> key)
//    }
//
//  }



}
