package controllers.circs.submission

import play.api.mvc._
import play.api.Logger
import com.google.inject._
import models.view.CachedChangeOfCircs
import services.UnavailableTransactionIdException
import controllers.submission.Submitter
import play.Configuration
import models.domain._
import models.domain.Claim

@Singleton
class ChangeOfCircsSubmissionController @Inject()(submitter: Submitter) extends Controller with CachedChangeOfCircs {

  def submit = claiming { implicit claim => implicit request =>
    if (isBot(claim)) {
      NotFound(views.html.errors.onHandlerNotFound(request)) // Send bot to 404 page.
    }
    else {
      try {
        Async {
          submitter.submit(claim, request)
        }
      }
      catch {
        case e: UnavailableTransactionIdException => {
          Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
          Redirect(s"/error?key=${CachedChangeOfCircs.key}")
        }
        case e: java.lang.Exception => {
          Logger.error(s"InternalServerError ! ${e.getMessage}")
          Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
          Redirect(s"/error?key=${CachedChangeOfCircs.key}")
        }
      }
    }
  }

  val checkForBot: Boolean = Configuration.root().getBoolean("checkForBot", false)

  def isBot(claim: Claim): Boolean = {
    if (checkForBot) checkTimeToCompleteAllSections(claim) || honeyPot(claim)
    else false
  }

  def checkTimeToCompleteAllSections(claim: Claim, currentTime: Long = System.currentTimeMillis()) = {
    val sectionExpectedTimes = Map[String, Long](
      // Change of circs
      "c1" -> 10000,
      "c2" -> 10000,
      "c3" -> 10000
    )

    val expectedMinTimeToCompleteAllSections: Long = claim.sections.map(s => {
      sectionExpectedTimes.get(s.identifier.id) match {
        case Some(n) => n
        case _ => 0
      }
    }).reduce(_ + _) // Aggregate all of the sectionExpectedTimes for completed sections only.

    val actualTimeToCompleteAllSections: Long = currentTime - claim.created
    //println("actual: " + actualTimeToCompleteAllSections + ", expected: " + expectedMinTimeToCompleteAllSections)
    actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections
  }

  def honeyPot(claim: Claim): Boolean = {
    def checkDeclaration: Boolean = {
      claim.questionGroup(CircumstancesDeclaration) match {
        case Some(q) => {
          val h = q.asInstanceOf[CircumstancesDeclaration]
          if (h.obtainInfoAgreement == "yes") {
            h.obtainInfoWhy match {
              case Some(f) => true // Bot given field howOftenPersonal was not visible.
              case _ => false
            }
          }
          else false
        }
        case _ => false
      }
    }

    checkDeclaration
  }
}