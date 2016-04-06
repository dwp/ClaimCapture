package controllers.preview

import controllers.PreviewRouteUtils
import play.api.Play._
import play.api.mvc.{Result, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.{Claim, PreviewModel}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import play.api.i18n._

object Preview extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "email" -> optional(text(60))
  )(PreviewModel.apply)(PreviewModel.unapply))

  def present = claiming { implicit claim => implicit request => implicit request2lang =>
    cleanPointOfEntry(
      track(models.domain.PreviewModel, beenInPreview = true) { implicit claim => Ok(views.html.preview.preview(form.fill(PreviewModel))) }
    )
  }

  def back = claiming { implicit claim => implicit request => implicit request2lang =>
    resetPreviewState { implicit claim => Redirect(claim.navigation.previous.toString) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      errors => BadRequest(views.html.preview.preview(errors)),
      data => claim.update(data) -> Redirect(controllers.s_consent_and_declaration.routes.GDeclaration.present())
    )
  }

  def redirect(id:String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    val hashValue = request.getQueryString("hash") match {
      case Some(hash) => hash
      case _ => ""
    }
    val returnToSummaryValue = request.getQueryString("returnToSummary") match {
      case Some(returnToSummary) => returnToSummary
      case _ => ""
    }

    val routesMap = PreviewRouteUtils.yourDetailsRoute ++
      PreviewRouteUtils.educationRoute ++ PreviewRouteUtils.careYouProvide ++ PreviewRouteUtils.breaks ++ PreviewRouteUtils.yourPartner ++
      PreviewRouteUtils.employmentRoute ++ PreviewRouteUtils.yourIncomeOtherPaymentsRoute ++ PreviewRouteUtils.bankDetailsRoute ++ PreviewRouteUtils.additionalInfoRoute ++ PreviewRouteUtils.thirdPartyRoute

    val updatedClaim = claim.copy(checkYAnswers = claim.checkYAnswers.copy(cyaPointOfEntry = Some(routesMap(id)), returnToSummaryAnchor = returnToSummaryValue))(claim.navigation)

    updatedClaim -> Redirect(routesMap(id)).flashing("hash"->hashValue)
  }

  /**
   * The Point of Entry is the designation of the page that we want to change in CYA and we link directly to it. It has to be cleared every time
   * entering the CYA page.
   * @param tuple (Claim,Result)
   * @return
   */
  private def cleanPointOfEntry(tuple:(Claim,Result)) = {
    val claim = tuple._1
    tuple.copy(_1 = claim.copy(checkYAnswers = claim.checkYAnswers.copy(cyaPointOfEntry = None, returnToSummaryAnchor = ""))(navigation = claim.navigation))
  }
}
