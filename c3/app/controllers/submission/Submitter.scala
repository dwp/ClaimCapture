package controllers.submission

import models.domain.Claim
import scala.concurrent.Future
import play.api.mvc.PlainResult

trait Submitter {
  def submit(claim: Claim): Future[PlainResult]
}
