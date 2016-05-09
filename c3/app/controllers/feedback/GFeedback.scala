package controllers.feedback

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.{OptYesNoWith2Text}
import play.api.i18n.{MMessages, MessagesApi, I18nSupport}
import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain._
import models.view.Navigable
import play.api.Logger
import play.api.Play._
import app.ConfigProperties._
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import play.api.mvc._

import scala.collection.mutable

object GFeedback extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val difficultyAndText =
    "difficultyAndText" -> mapping(
      "answer" -> {
        optional(text.verifying(validYesNo))
      },
      "text1" -> optional(carersNonEmptyText(maxLength = 3000)),
      "text2" -> optional(carersNonEmptyText(maxLength = 3000))
    )(OptYesNoWith2Text.apply)(OptYesNoWith2Text.unapply)

  val form = Form(mapping(
    "satisfiedAnswer" -> {
      nonEmptyText
    },
    difficultyAndText
  )
  (Feedback.apply)(Feedback.unapply))

  def present = optionalClaim { implicit claim => implicit request => implicit request2lang =>
    getBooleanProperty("feedback.cads.enabled") match {
      case false => BadRequest(views.html.common.switchedOff("feedback-present", request2lang))
      case true => Ok(views.html.feedback.feedback(form))
    }
  }

  def submit(claimOrCircs: String) = optionalClaim { implicit claim => implicit request => implicit request2lang =>
    getBooleanProperty("feedback.cads.enabled") match {
      case false => BadRequest(views.html.common.switchedOff("feedback-submit", request2lang))
      case true => {
        form.bindEncrypted.fold(
          formWithErrors => {
            BadRequest(views.html.feedback.feedback(formWithErrors))
          },
          form => {
            try {
              val objectMapper: ObjectMapper = new ObjectMapper
              val jsonString = objectMapper.writeValueAsString(populateFeedbackCacheObject(form, request, claimOrCircs))
              processFeedback(jsonString)
              Redirect(thankyouPageUrl)
            }
            catch {
              case e: JsonProcessingException => {
                Logger.error("Feedback failed to create json and save to cache from feedback object exception:" + e.toString)
                BadRequest(views.html.feedback.feedbackError(request2lang))
              }
            }
          }
        )
      }
    }
  }

  /* Note that changes to this object and thus the json string created may need to be reflected in the CarersUtils reporting suite
     But since we are using a simple java hashmap, adding new fields will not break the CarersUtils.
  * */
  def populateFeedbackCacheObject(form: Feedback, request: Request[AnyContent], claimOrCircs: String) = {
    val feedbackVersion = "1.0"

    val feedbackCacheObject = new java.util.HashMap[String, String]()
    feedbackCacheObject.put("feedbackversion", feedbackVersion)
    feedbackCacheObject.put("datesecs", form.datetimesecs.toString)
    feedbackCacheObject.put("origin", form.origin)
    feedbackCacheObject.put("claimOrCircs", claimOrCircs)
    feedbackCacheObject.put("satisfiedScore", form.satisfiedScore.toString)
    feedbackCacheObject.put("difficulty", form.difficultyAndText.answer.getOrElse(""))
    feedbackCacheObject.put("comment", form.difficultyAndText.text)
    feedbackCacheObject.put("useragent", request.headers.get("User-Agent").getOrElse(""))
    feedbackCacheObject
  }

  def processFeedback(json: String) = {
    saveFeedbackInCache(json)
  }

  def thankyouPageUrl = {
    app.ConfigProperties.getStringProperty("origin.tag") match {
      case "GB" => getStringProperty("feedback.gb.thankyou.url")
      case "GB-NIR" => getStringProperty("feedback.ni.thankyou.url")
      case _ => "config-error-origin-tag"
    }
  }
}
