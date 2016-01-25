package controllers.feedback

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.{OptYesNoWith2Text, YesNo, YesNoWith2Text}
import play.api.i18n.{Lang, MMessages, MessagesApi, I18nSupport}
import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Controller}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain._
import models.view.Navigable
import play.api.Logger
import play.api.Play._
import app.ConfigProperties._

object GFeedback extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val difficultyAndText =
    "difficultyAndText" -> mapping(
      "answer" -> {
        optional(text.verifying(validYesNo))
      },
      "text1" -> optional(carersNonEmptyText(maxLength = 35)),
      "text2" -> optional(carersNonEmptyText(maxLength = 35))
    )(OptYesNoWith2Text.apply)(OptYesNoWith2Text.unapply)

  val form = Form(mapping(
    "satisfiedAnswer" -> {
      nonEmptyText
    },
    difficultyAndText
  )
  (Feedback.apply)(Feedback.unapply))

  val form2 = Form(mapping(
    "benefitsAnswer" -> nonEmptyText
  )(Benefits.apply)(Benefits.unapply))

  def present = newClaim { implicit claim => implicit request => implicit request2lang =>
    getProperty("feedback.cads.enabled", default = false) match {
      case false => BadRequest(views.html.common.switchedOff("feedback-present", request2lang))
      case true => Ok(views.html.feedback.feedback(form))
    }
  }

  def submit = newClaim {implicit claim => implicit request => implicit request2lang =>
    getProperty("feedback.cads.enabled", default = false) match {
      case false => BadRequest(views.html.common.switchedOff("feedback-submit", request2lang))
      case true => {
        form.bindEncrypted.fold(
          formWithErrors => {
            println("Form got errors:"+formWithErrors)
            BadRequest(views.html.feedback.feedback(formWithErrors))
          },
          form => {
//            processFeedback(form)
            Redirect(thankyouPageUrl)
          }
        )
      }
    }
  }

  def processFeedback(form: Form[Feedback]) = {
//    saveFeedbackInCache()
  }

  def thankyouPageUrl={
    app.ConfigProperties.getProperty("origin.tag", "GB") match{
      case "GB" => getProperty("feedback.gb.thankyou.url", default = "config-error-getting-uk-thankyou")
      case "NI" => getProperty("feedback.ni.thankyou.url", default = "config-error-getting-ni-thankyou")
      case _ => "config-error-origin-tag"
    }
  }
}
