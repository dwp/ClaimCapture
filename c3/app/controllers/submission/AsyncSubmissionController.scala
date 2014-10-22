package controllers.submission

import play.api.mvc.{Request, AnyContent}
import models.domain.Claim
import play.api.Logger
import monitoring.BotChecking

trait AsyncSubmissionController {

  this: BotChecking =>

  def checkForBot(claim: Claim, request: Request[AnyContent]) {
    if (isHoneyPotBot(claim)) {
      // Only log honeypot for now.
      // May send to an error page in the future
      Logger.warn(s"Honeypot ! ${claim.key} ${claim.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
    }

    if (isSpeedBot(claim)) {
      // Only log speed check for now.
      // May send to an error page in the future
      Logger.warn(s"Speed check ! ${claim.key} ${claim.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
    }
  }
}

