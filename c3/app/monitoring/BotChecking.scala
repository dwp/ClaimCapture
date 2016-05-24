package monitoring

import app.ConfigProperties._
import models.domain.Claim
import play.api.Logger

trait BotChecking {

  def checkTimeToCompleteAllSections(claimOrCircs: Claim, currentTime: Long): Boolean

  def honeyPot(claim: Claim): Boolean

  def calculateActualTimeToCompleteAllSections(currentTime: Long, created: Long): Long

  def isSpeedBot(claimOrCircs: Claim): Boolean = {
    val checkForBotSpeed = getBooleanProperty("checkForBotSpeed")
    checkForBotSpeed && checkTimeToCompleteAllSections(claimOrCircs, System.currentTimeMillis())
  }

  def isHoneyPotBot(claimOrCircs: Claim): Boolean = {
    val checkForBotHoneyPot = getBooleanProperty("checkForBotHoneyPot")
    checkForBotHoneyPot && honeyPot(claimOrCircs)
  }

  def evaluateTimeToCompleteAllSections(claim: Claim, currentTime: Long = System.currentTimeMillis(), sectionExpectedTimes: Map[String, Long]) = {

    val expectedMinTimeToCompleteAllSections: Long = claim.sections.map(s => {
      sectionExpectedTimes.get(s.identifier.id) match {
        case Some(n) =>
          if (s.questionGroups.size > 0) n else 0
        case _ => 0
      }
    }).reduce(_ + _) // Aggregate all of the sectionExpectedTimes for completed sections only.

    val actualTimeToCompleteAllSections: Long = calculateActualTimeToCompleteAllSections(currentTime, claim.created)

    val result = actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections

    if (result) {
      Logger.error(s"Detected bot completing sections too quickly! ${claim.key} ${claim.uuid}. actualTimeToCompleteAllSections: $actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections: $expectedMinTimeToCompleteAllSections")
    }
    result
  }
}
