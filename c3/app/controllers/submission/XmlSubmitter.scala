package controllers.submission

import play.api.mvc.Results.Ok
import models.domain.Claim
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, PlainResult}
import ExecutionContext.Implicits.global
import xml.{DWPCAClaim}

class XmlSubmitter extends Submitter {
  def submit(claim: Claim, request : Request[AnyContent]): Future[PlainResult] = {
    val claimXml = DWPCAClaim.xml(claim, "TEST999")
    Future(Ok(claimXml.buildString(stripComments = false)))
  }

}
