package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.JSEnabled
import play.api.Logger
import controllers.Mappings._
import utils.helpers.CarersForm._

object CarersAllowance extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "answerRequired" -> boolean,
    "answer" -> optional(nonEmptyText.verifying(validYesNo)),
    "jsEnabled" -> boolean
  )(ProceedAnyway.apply)(ProceedAnyway.unapply))

  def approve = claiming {implicit claim => implicit request => implicit lang =>
    val benefitsAnswer = claim.questionGroup[Benefits].getOrElse(Benefits()).asInstanceOf[BooleanConfirmation].answer
    val hoursAnswer = claim.questionGroup[Hours].getOrElse(Hours()).asInstanceOf[BooleanConfirmation].answer
    val over16Answer = claim.questionGroup[Over16].getOrElse(Over16()).asInstanceOf[BooleanConfirmation].answer
    val livesInGBAnswer = claim.questionGroup[LivesInGB].getOrElse(LivesInGB()).asInstanceOf[BooleanConfirmation].answer
    val completedQuestionGroups = List(benefitsAnswer, hoursAnswer, over16Answer, livesInGBAnswer)
    val approved = completedQuestionGroups.length == 4 && completedQuestionGroups.forall(_.asInstanceOf[Boolean])

    track(LivesInGB) { implicit claim => Ok(views.html.s1_carers_allowance.g6_approve(form.fill(ProceedAnyway), approved, benefitsAnswer, hoursAnswer, over16Answer, livesInGBAnswer)) }
  }

  def approveSubmit = claiming {implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val benefitsAnswer = claim.questionGroup[Benefits].getOrElse(Benefits()).asInstanceOf[BooleanConfirmation].answer
        val hoursAnswer = claim.questionGroup[Hours].getOrElse(Hours()).asInstanceOf[BooleanConfirmation].answer
        val over16Answer = claim.questionGroup[Over16].getOrElse(Over16()).asInstanceOf[BooleanConfirmation].answer
        val livesInGBAnswer = claim.questionGroup[LivesInGB].getOrElse(LivesInGB()).asInstanceOf[BooleanConfirmation].answer
        val completedQuestionGroups = List(benefitsAnswer, hoursAnswer, over16Answer, livesInGBAnswer)
        val approved = completedQuestionGroups.length == 4 && completedQuestionGroups.forall(_.asInstanceOf[Boolean])

        BadRequest(views.html.s1_carers_allowance.g6_approve(formWithErrors, approved, benefitsAnswer, hoursAnswer, over16Answer, livesInGBAnswer))
      },
      f => {
        if (!f.jsEnabled) {
          Logger.info(s"No JS - Start ${claim.key} User-Agent : ${request.headers.get("User-Agent").orNull}")
        }

        if (f.answerRequired) {
          claim.update(f) -> Redirect(controllers.s1_2_claim_date.routes.G1ClaimDate.present())
        } else {
          f.answerYesNo match {
            case Some(proceed) if (proceed == "yes") => claim.update(f) -> Redirect(controllers.s1_2_claim_date.routes.G1ClaimDate.present())
            case _ => claim.update(f) -> Redirect("http://www.gov.uk/done/apply-carers-allowance")
          }
        }
      }
    )
  }
}