package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import controllers.Mappings._
import utils.helpers.CarersForm._
import scala.Some

object CarersAllowance extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "allowedToContinue" -> boolean,
    "answer" -> optional(nonEmptyText.verifying(validYesNo)),
    "jsEnabled" -> boolean
  )(ProceedAnyway.apply)(ProceedAnyway.unapply)
    .verifying("error.required", mandatoryChecks _))

  def approve = claiming {implicit claim =>  implicit request =>  lang =>
    track(LivesInGB) { implicit claim => Ok(views.html.s1_carers_allowance.g6_approve(form.fill(ProceedAnyway))(lang)) }
  }

  def approveSubmit = claiming {implicit claim =>  implicit request =>  lang =>
    Logger.info("Approve submit, request:"+request.body)
    form.bindEncrypted.fold(
      formWithErrors => {
        Logger.info("Form with errors: "+formWithErrors)
        BadRequest(views.html.s1_carers_allowance.g6_approve(formWithErrors)(lang))
      },
      f => {
        if (!f.jsEnabled) {
          Logger.info(s"No JS - Start ${claim.key} ${claim.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
        }

        f.answer match {
          case true => claim.update(f) -> Redirect(controllers.s1_2_claim_date.routes.G1ClaimDate.present())
          case _ => claim.update(f) -> Redirect("http://www.gov.uk/done/apply-carers-allowance")
        }
      }
    )
  }

  def mandatoryChecks(proceedAnyway: ProceedAnyway) = {
    proceedAnyway.allowedToContinue match {
      case true => true
      case false if (proceedAnyway.answerYesNo.isDefined) => true
      case _ => false
    }
  }
}