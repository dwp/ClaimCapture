package controllers.save_for_later

import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.mappings.NINOMappings._
import controllers.s_about_you.GNationalityAndResidency._
import controllers.s_about_you.routes
import models.domain._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.{Form}
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc.{Controller}
import utils.helpers.CarersCrypto
import utils.helpers.CarersForm._
import app.ConfigProperties._
import scala.language.reflectiveCalls

object GSaveForLater extends Controller with CachedClaim with Navigable with I18nSupport {

  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
  Ok(views.html.save_for_later.saveClaimSuccess(lang))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    //val saveForLaterPageData = request.queryString.map { case (k,v) => CarersCrypto.decryptAES(k) -> v.mkString }

    val params=request.body.asFormUrlEncoded.get
    var paramsmap: Map[String,String]=Map()
    params.keys.foreach { key =>
      paramsmap+=(key->params.get(key).get(0))
    }

    if( getProperty("saveForLaterEnabled",default = false) ){
      var updatedClaim=claim.update(paramsmap)
      updatedClaim->Redirect( controllers.save_for_later.routes.GSaveForLater.present())
    }
    else{
      BadRequest(views.html.save_for_later.saveClaimSuccess(lang))
    }
  }
}
