package controllers.circs.origin

import controllers.CarersForms._
import controllers.submission.AsyncSubmissionController
import models.domain.ReportChangeOrigin
import models.view.{CachedChangeOfCircs, Navigable}
import monitoring.ChangeBotChecking
import play.api.Logger
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.Controller
import services.ClaimTransactionComponent
import services.submission.ClaimSubmissionService
import utils.helpers.CarersForm._

import scala.language.{postfixOps, reflectiveCalls}

object GOrigin extends Controller with CachedChangeOfCircs with Navigable with I18nSupport
with AsyncSubmissionController
with ChangeBotChecking
with ClaimSubmissionService
with ClaimTransactionComponent{
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val claimTransaction = new ClaimTransaction

  /*
    We need to handle CIRCS cross site bleed between GB and NI sites which are the same code but running with different origin tags in properties.
    If Site is GB-NIR we present country select list.
    If GB-NIR Site and we are posted country=NI we take user to Circs ( Ni Circs )
    If GB-NIR Site and we are posted country of GB or Other we present Origin Error Page.

    If GB Site then we are in error, we dont expect this. But of course its possible.
    So we will take user to the Circs page on present, or error page on Submit.
 */
  val form = Form(mapping(
    "origin" -> carersNonEmptyText(maxLength = 20)
  )(ReportChangeOrigin.apply)(ReportChangeOrigin.unapply))


  def present = optionalClaim { implicit circs => implicit request => lang =>
    Logger.info(s"Starting Origin Select")
    app.ConfigProperties.getProperty("origin.tag", "GB") match {
      case "GB-NIR" => Ok(views.html.circs.origin.origin(form.fill(ReportChangeOrigin)))
      case _ => {
        Logger.error("Origin Select - Unexpected access to Origin Select on GB site. Redirecting to GB Circs")
        circs -> Redirect(controllers.circs.start_of_process.routes.GReportChangeReason.present())
      }
    }
  }

  def submit = optionalClaim { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.circs.origin.origin(formWithErrors))
      },
      f => {
        (app.ConfigProperties.getProperty("origin.tag", "GB"), f.origin) match {
          case ("GB-NIR", "NI") => {
            Logger.info("Origin Select - NISSA site user posted selected originating country of:" + f.origin + ". Redirecting to NI Circs")
            circs -> Redirect(controllers.circs.start_of_process.routes.GReportChangeReason.present())
          }
          case ("GB-NIR", _) => {
            Logger.info("Origin Select - NISSA site user posted selected originating country of:" + f.origin + ". Displaying origin error page")
            Ok(views.html.common.origin.NIoriginError(true, request2lang))
          }
          case _ => {
            Logger.error("Origin Select - GB site user posted selected originating country of:" + f.origin + ". Displaying origin error page")
            Ok(views.html.common.origin.GBoriginError(true, request2lang))
          }
        }
      }
    )
  }
}
