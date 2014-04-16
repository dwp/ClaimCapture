package controllers.submission

import play.api.mvc.Results.Ok
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, SimpleResult}
import ExecutionContext.Implicits.global
import play.Configuration
import models.domain.Claim
import xml.DWPBody


object XmlSubmitter {
  val transactionID = "TEST432"

  def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = {
    val validator = xmlValidator(claim)
    val fullXml = DWPBody().xml(claim,transactionID )

    if (Configuration.root().getBoolean("validateXml", true)) {
      val fullXmlString = fullXml.buildString(stripComments = false)
      validator.validate(fullXmlString) match {
        case true => Future(Ok(fullXml.buildString(stripComments = false)))
        case false => Future(Ok("Failed validation"))
      }
    } else {
      Future(Ok(fullXml.buildString(stripComments = false)))
    }

  }
}