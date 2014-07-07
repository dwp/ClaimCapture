package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.JSEnabled
import play.api.Logger

object CarersAllowance extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jsEnabled" -> boolean
  )(JSEnabled.apply)(JSEnabled.unapply))

  def approve = claiming {implicit claim => implicit request => implicit lang =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CarersAllowance)
    val approved = completedQuestionGroups.length == 4 && completedQuestionGroups.forall(_.asInstanceOf[BooleanConfirmation].answer)

    track(LivesInGB) { implicit claim => Ok(views.html.s1_carers_allowance.g6_approve(approved)) }
  }

  def approveSubmit = claiming {implicit claim => implicit request => implicit lang =>
    form.bindFromRequest.fold(
      formWithErrors => {
        NotFound
      },
      f => {
        if (!f.jsEnabled) {
          Logger.info(s"No JS - Start ${claim.key} User-Agent : ${request.headers.get("User-Agent").orNull}")
        }
        Redirect(controllers.s1_2_claim_date.routes.G1ClaimDate.present())
      }
    )
  }
}