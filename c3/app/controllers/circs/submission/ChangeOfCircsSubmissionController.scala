package controllers.circs.submission

import app.ConfigProperties._
import play.api.mvc._
import play.api.Logger
import com.google.inject._
import models.view.CachedChangeOfCircs
import services.UnavailableTransactionIdException
import controllers.submission.Submitter
import play.Configuration
import models.domain._
import jmx.inspectors.{FastSubmissionNotifier, SubmissionNotifier}
import models.domain.Claim
import scala.Some

@Singleton
class ChangeOfCircsSubmissionController @Inject()(submitter: Submitter) extends Controller with CachedChangeOfCircs with SubmissionNotifier with FastSubmissionNotifier {

  def submit = claiming { implicit circs => implicit request =>
    if (isBot(circs)) {
      NotFound(views.html.errors.onHandlerNotFound(request)) // Send bot to 404 page.
    }
    else {
      try {
        Async {
          fireNotification(circs) { submitter.submit(circs, request) }
        }
      }
      catch {
        case e: UnavailableTransactionIdException =>
          Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
          Redirect(controllers.routes.Application.error(CachedChangeOfCircs.key))

        case e: java.lang.Exception =>
          Logger.error(s"InternalServerError ! ${e.getMessage}")
          Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
          Redirect(controllers.routes.Application.error(CachedChangeOfCircs.key))

      }
    }
  }

  def isBot(claim: Claim): Boolean = {
    if (getProperty("checkForBot",default=false)) {
      checkTimeToCompleteAllSections(claim) || honeyPot(claim)
    } else {
      false
    }
  }

  def checkTimeToCompleteAllSections(circs: Claim, currentTime: Long = System.currentTimeMillis()) = {
    val sectionExpectedTimes = Map[String, Long](
      "c1" -> getProperty("speed.c1",5000L),
      "c2" -> getProperty("speed.c2",5000L),
      "c3" -> getProperty("speed.c3",5000L)
    )

    val expectedMinTimeToCompleteAllSections: Long = circs.sections.map(s => {
      sectionExpectedTimes.get(s.identifier.id) match {
        case Some(n) => n
        case _ => 0
      }
    }).reduce(_ + _) // Aggregate all of the sectionExpectedTimes for completed sections only.

    val actualTimeToCompleteAllSections: Long = currentTime - circs.created
    val result = actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections

    if (result) {
      fireFastNotification(circs)
      Logger.error(s"Detected bot completing sections too quickly! actualTimeToCompleteAllSections: $actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections: $expectedMinTimeToCompleteAllSections")
    }

    result
  }

  def honeyPot(circs: Claim): Boolean = {
    def checkDeclaration: Boolean = {
      circs.questionGroup(CircumstancesDeclaration) match {
        case Some(q) =>
          val h = q.asInstanceOf[CircumstancesDeclaration]
          if (h.obtainInfoAgreement == "yes") {
            h.obtainInfoWhy match {
              case Some(f) => true // Bot given field howOftenPersonal was not visible.
              case _ => false
            }
          }
          else false

        case _ => false
      }
    }

    checkDeclaration
  }
}