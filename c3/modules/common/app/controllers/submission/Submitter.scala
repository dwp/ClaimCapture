package controllers.submission

import scala.concurrent.Future
import play.api.mvc.{AnyContent, Request, PlainResult}
import models.domain.Claim

trait Submitter {
  def submit(claim: Claim, request : Request[AnyContent]): Future[PlainResult]
}
