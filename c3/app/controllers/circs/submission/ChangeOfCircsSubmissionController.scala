package controllers.circs.submission

import app.ConfigProperties._
import com.google.inject._
import controllers.submission.{SubmissionController, Submitter}
import models.domain._
import models.domain.Claim
import scala.Some
import models.view.CachedChangeOfCircs
import play.api.Logger

@Singleton
class ChangeOfCircsSubmissionController @Inject()(submitter: Submitter) extends SubmissionController(submitter) with CachedChangeOfCircs{

  def submit = submitting { implicit claim => implicit request =>
    processSubmit(claim, request, errorPage)
  }

  def checkTimeToCompleteAllSections(circs: Claim with Claimable, currentTime: Long = System.currentTimeMillis()) = {
    val sectionExpectedTimes = Map[String, Long](
      "c1" -> getProperty("speed.c1",5000L),
      "c2" -> getProperty("speed.c2",5000L),
      "c3" -> getProperty("speed.c3",5000L)
    )
    evaluateTimeToCompleteAllSections(circs, currentTime, sectionExpectedTimes)
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

    val declaration = checkDeclaration

    if (declaration) Logger.warn("Honeypot triggered coc : declaration")

    declaration
  }
}