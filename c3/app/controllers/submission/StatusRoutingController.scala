package controllers.submission

import play.api.mvc.Controller
import models.view.CachedClaim
import services.ClaimTransactionComponent


object StatusRoutingController extends Controller with CachedClaim with ClaimTransactionComponent {

  val claimTransaction = new ClaimTransaction

  def submit = claiming { implicit claim => implicit request => implicit lang =>

    Redirect(controllers.routes.ClaimEnding.thankyou)
  }
}
