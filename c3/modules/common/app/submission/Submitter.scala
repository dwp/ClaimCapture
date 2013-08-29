package submission

import models.domain.Claim
import scala.concurrent.Future
import play.api.mvc.{AnyContent, Request, PlainResult}

trait Submitter {
  def submit(claim: Claim, request : Request[AnyContent]): Future[PlainResult]
}
