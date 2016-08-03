package controllers.breaks_in_care

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc.Controller

import scala.language.postfixOps

object GBreaksInCare extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "answer" -> nonEmptyText.verifying(validYesNo)
  )(OldBreaksInCareSummary.apply)(OldBreaksInCareSummary.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(OldBreaksInCare) { implicit claim => Ok(views.html.breaks_in_care.breakType(form.fill(OldBreaksInCareSummary), breaksInCare)) }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[OldBreaksInCare].getOrElse(OldBreaksInCare())

  def submit = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    Ok(views.html.breaks_in_care.breaksInCare(form.fill(OldBreaksInCareSummary), breaksInCare))
  }.withPreviewConditionally[OldBreaksInCareSummary]((breaksInCareSummary,claims) => breaksInCareSummary._2.answer == Mappings.no)

  def delete = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    Ok(views.html.breaks_in_care.breaksInCare(form.fill(OldBreaksInCareSummary), breaksInCare))
  }
}
