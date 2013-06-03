package controllers

import play.api.mvc._
import models.view.example._
import utils.CacheUtil
import scala.Predef._
import play.api.data._
import play.api.data.Forms._
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
        val claim = CacheUtil.loadFromCache(key)
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

          val claim = CacheUtil.loadFromCache(key)
          val sectionOption = claim.getSectionWithId(sectionId)
          val section = sectionOption.get
          form.bindFromRequest.fold(
            formWithErrors => {
              section.name match {
                case "sectionOne" => BadRequest(views.html.sectionOne(section, formWithErrors))
                case "sectionTwo" => BadRequest(views.html.sectionTwo(section, formWithErrors))
              }
            },

            singleStringInputForm => {
              var sectionToNavigateTo = sectionId
              val nextQuestionGroupOption = section.getNextUnansweredQuestionGroup
              val nextQuestionGroup = nextQuestionGroupOption.get

              val newClaim = Claim(claim.sections.map {
                section => {
                  section.name match {
                    case sectionId => {
                      new Section(sectionId, section.questionGroups.map {
                        questionGroup => {
                          questionGroup.label match {
                            case nextQuestionGroup.label => {
                              new QuestionGroup(questionGroup.label, true, singleStringInputForm)
                            }
                            case _ => questionGroup
                          }
                        }
                      })
                    }
                    case _ => section
                  }
                }
              }
              )

              CacheUtil.updateCache(key, newClaim)

              val updatedSectionOption = newClaim.getSectionWithId(sectionId)

              if (updatedSectionOption.isDefined && updatedSectionOption.get.isComplete) {
                val nextSectionOption = newClaim.getNextIncompleteSection
                if (nextSectionOption.isDefined) {
                  val nextSection = nextSectionOption.get
                  sectionToNavigateTo = nextSection.name
                }
              }

              if (newClaim.getNextIncompleteSection.isEmpty) {
                CacheUtil.updateCache(key, Claim())
                Ok(views.html.index("Thank you for filling in the Carers Claim"))
              }
              else {
                Redirect(routes.ClaimController.presenter(sectionToNavigateTo))
              }

            }
          )
      }.getOrElse {
        val key = java.util.UUID.randomUUID().toString
        Redirect(routes.ClaimController.presenter(sectionId)).withSession("connected" -> key)
      }
  }
}
