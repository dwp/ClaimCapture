package controllers.submission

import play.api.mvc.Results.Ok
import models.domain.Claim
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.PlainResult
import ExecutionContext.Implicits.global
import services.submission.ClaimSubmission

class XmlSubmitter extends Submitter {
  def submit(claim: Claim): Future[PlainResult] = {
    val claimXml = ClaimSubmission(claim, "TEST999").buildDwpClaim
    Future(Ok(claimXml.buildString(stripComments = false)))
  }
}
