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

      println("present: " + sectionId)

      val claim =  CacheUtil.loadFromCache(key).get
      val sectionOption = claim.getSectionWithId(sectionId)

      if(sectionOption.isDefined) {

        sectionOption.get.name match {
          case "sectionOne" => Ok(views.html.sectionOne(sectionOption.get, form))
          case "sectionTwo" => Ok(views.html.sectionTwo(sectionOption.get, form))
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



}
