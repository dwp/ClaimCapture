package controllers.submission

import play.api.mvc._
import play.api.Logger
import com.google.inject._
import models.view.CachedClaim
import services.UnavailableTransactionIdException
import play.api.mvc.Results._

@Singleton
class SubmissionController @Inject()(submitter: Submitter) extends Controller with CachedClaim {

  def submit = claiming { implicit claim => implicit request =>
    try {
      Async {
        submitter.submit(claim, request)
      }
    }
    catch {
      case e: UnavailableTransactionIdException => {
        Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
        Redirect("/error")
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
        Redirect("/error")
      }
    }
  }

  def circumstancesSubmit = claimingCircs { implicit claim => implicit request =>
    Ok("TODO need to be make submission service for circumstances") // TODO need to be make submission service for circumstances

    /*try {
      Async {
        submitter.submit(claim, request)
      }
    }
    catch {
      case e: UnavailableTransactionIdException => {
        Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
        Redirect("/error")
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
        Redirect("/error")
      }
    }*/
  }
}