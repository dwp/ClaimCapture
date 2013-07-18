package controllers.s8_other_money

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ Claim, MoneyPaidToSomeoneElseForYou }
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2MoneyPaidToSomeoneElseForYou extends Controller with Routing with CachedClaim {
  override val route = MoneyPaidToSomeoneElseForYou.id -> routes.G2MoneyPaidToSomeoneElseForYou.present
  
  val form = Form(
    mapping(
      "moneyAddedToBenefitSinceClaimDate" -> nonEmptyText.verifying(validYesNo)
    )(MoneyPaidToSomeoneElseForYou.apply)(MoneyPaidToSomeoneElseForYou.unapply))
    
  def present = claiming { implicit claim => implicit request =>
    Ok(<p>Hello, World!</p>)

  }
}