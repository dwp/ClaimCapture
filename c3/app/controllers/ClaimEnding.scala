package controllers

import play.api.mvc.Controller
import models.view.CachedClaim
import app.ConfigProperties._
import services.mail.{SendEmail, EmailActors}
import services.EmailServices
import models.domain.PreviewModel

object ClaimEnding extends Controller with CachedClaim {

  def timeout = ending {implicit claim => implicit request  => implicit lang =>
    Ok(views.html.common.session_timeout(startPage))
  }

  def error = ending {implicit claim => implicit request  => implicit lang =>
    Ok(views.html.common.error(startPage))
  }

  def thankyou = ending { implicit claim => implicit request  => implicit lang =>

    if (getProperty("mailer.enabled",false)){
      val preview = claim.questionGroup[PreviewModel].getOrElse(PreviewModel())
      if (preview.email.isDefined) EmailServices.sendEmail to preview.email.get
    }

    Ok(views.html.common.thankYouClaim())
  }

}
