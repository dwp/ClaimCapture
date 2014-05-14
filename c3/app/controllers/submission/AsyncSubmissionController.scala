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
      Logger.warn(s"Honeypot ! User-Agent : ${request.headers.get("User-Agent").orNull}")
    }

    if (isSpeedBot(claim)) {
      // Only log speed check for now.
      // May send to an error page in the future
      Logger.warn(s"Speed check ! User-Agent : ${request.headers.get("User-Agent").orNull}")
    }
  }
}

