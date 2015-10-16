package controllers.preview

import controllers.PreviewRouteUtils
import play.api.Logger
import play.api.mvc.{Result, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.{Claim, PreviewModel}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object Preview extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "email" -> optional(text(60))
  )(PreviewModel.apply)(PreviewModel.unapply))

  def present = claiming { implicit claim => implicit request => lang =>
    cleanPointOfEntry(
      track(models.domain.PreviewModel, beenInPreview = true) { implicit claim => Ok(views.html.preview.preview(form.fill(PreviewModel))(lang)) }
    )
  }

  def back = claiming { implicit claim => implicit request => implicit lang =>
    resetPreviewState { implicit claim => Redirect(claim.navigation.previousIgnorePreview.toString) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => lang =>
    form.bindEncrypted.fold(
      errors => BadRequest(views.html.preview.preview(errors)(lang)),
      data => claim.update(data) -> Redirect(controllers.s_consent_and_declaration.routes.GDeclaration.present())
    )
  }

  def redirect(id:String) = claimingWithCheck { implicit claim => implicit request => lang =>

    val hashValue = request.getQueryString("hash") match {
      case Some(hash) => hash
      case _ => ""
    }


    val routesMap =     PreviewRouteUtils.yourDetailsRoute ++ PreviewRouteUtils.otherMoneyRoute ++
      PreviewRouteUtils.educationRoute ++ PreviewRouteUtils.careYouProvide ++ PreviewRouteUtils.breaks ++ PreviewRouteUtils.yourPartner ++
      PreviewRouteUtils.employmentRoute ++ PreviewRouteUtils.additionalInfoRoute


    val updatedClaim = claim.copy(checkYAnswers = claim.checkYAnswers.copy(cyaPointOfEntry = Some(routesMap(id))))(claim.navigation)

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
    tuple.copy(_1 = claim.copy(checkYAnswers = claim.checkYAnswers.copy(cyaPointOfEntry = None))(navigation = claim.navigation))
  }
}
