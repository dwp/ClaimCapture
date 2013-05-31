package controllers

import play.api.mvc._
import utils.CacheUtil
import play.api.data.Form
import play.api.data.Forms._
import models.view.example.SingleStringInputForm

object PresenterController extends Controller {

  val form =
    Form(
      mapping(
        "answer" -> text
      )(SingleStringInputForm.apply)(SingleStringInputForm.unapply)
    )


  def present(sectionId:String) = Action {
    request => request.session.get("connected").map { key =>

      val claim =  CacheUtil.loadFromCache(key).get
      val sectionOption = claim.getSectionWithId(sectionId)

      if(sectionOption.isDefined) {

        sectionOption.get.name match {
          case "sectionOne" => Ok(views.html.sectionOne(sectionOption.get, form))
          case "sectionTwo" => Ok(views.html.sectionTwo(sectionOption.get, form))
//          case "sectionOne" => Ok(views.html.sectionOne(answeredQuestionGroups, nextQuestionGroup, form))
//          case "sectionTwo" => Ok(views.html.sectionTwo(answeredQuestionGroups, nextQuestionGroup, form))
        }
      }
      else {
        NotFound
      }
    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Redirect(routes.PresenterController.present(sectionId)).withSession("connected" -> key)
    }
  }


//  def present(sessionId:String) = Action {
//    request => request.session.get("connected").map { key =>
//
//      val claim =  CacheUtil.loadFromCache(key).get
//      val sectionOption = claim.getNextIncompleteSection()
//
//      if(sectionOption.isDefined) {
//        val section = sectionOption.get
//        val answeredQuestionGroups = section.getAnsweredQuestionGroups()
//        val nextQuestionGroup = section.getNextUnansweredQuestionGroup.getOrElse(answeredQuestionGroups.last)
//
//        println("sectionName: " + section.name)
//
//        section.name match {
//          case "sectionOne" => Ok(views.html.sectionOne(answeredQuestionGroups, nextQuestionGroup, form))
//          case "sectionTwo" => Ok(views.html.sectionTwo(answeredQuestionGroups, nextQuestionGroup, form))
//          case _ => Ok(views.html.index("No match for section: " + sectionOption.get.name))
//        }
//      }
//      else {
//        Ok(views.html.index("Thank you"))
//      }
//
//
//    }.getOrElse {
//      val key = java.util.UUID.randomUUID().toString
//      Redirect(routes.PresenterController.present()).withSession("connected" -> key)
//    }
//
//
//  }

//  def sectionTwo() = Action {
//    request => request.session.get("connected").map { key =>
//
//      val claim =  CacheUtil.loadFromCache(key).get
//      val sectionOption = claim.getNextIncompleteSection()
//
//      if(sectionOption.isDefined) {
//        val answeredQuestionGroups = sectionOption.get.getAnsweredQuestionGroups()
//        val nextQuestionGroup = sectionOption.get.getNextUnansweredQuestionGroup.getOrElse(answeredQuestionGroups.last)
//        Ok(views.html.sectionTwo(answeredQuestionGroups, nextQuestionGroup, form))
//      }
//      else {
//        Ok(views.html.index("Thank you"))
//      }
//
//
//    }.getOrElse {
//      val key = java.util.UUID.randomUUID().toString
//      Redirect(routes.CommandController$.presenter()).withSession("connected" -> key)
//    }
//
//
//  }

}
