package monitoring

import app.ConfigProperties._
import models.domain.{CircumstancesDeclaration, Claim}
import play.api.Logger

trait ChangeBotChecking extends BotChecking {

  def checkTimeToCompleteAllSections(circs: Claim, currentTime: Long = System.currentTimeMillis()) = {
    val sectionExpectedTimes = Map[String, Long](
      "c1" -> getIntProperty("speed.c1").toLong,
      "c2" -> getIntProperty("speed.c2").toLong,
      "c3" -> getIntProperty("speed.c3").toLong
    )
    evaluateTimeToCompleteAllSections(circs, currentTime, sectionExpectedTimes)
  }

  def calculateActualTimeToCompleteAllSections(currentTime: Long, created: Long): Long = {
    val actualTimeToCompleteAllSections: Long = currentTime - created
    Histograms.recordChangeOfCircsSubmissionTime(actualTimeToCompleteAllSections)
    actualTimeToCompleteAllSections
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

        case None =>
          false
      }
    }

    val declaration = checkDeclaration

    if (declaration) Logger.warn(s"Honeypot triggered coc : declaration. ${circs.key} ${circs.uuid}")

    declaration
  }

}
