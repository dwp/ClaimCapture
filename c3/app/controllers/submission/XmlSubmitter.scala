package controllers.submission

import play.api.mvc.Results.Ok
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, PlainResult}
import ExecutionContext.Implicits.global
import scala.xml.Elem
import play.Configuration
import models.domain.Claim
import com.dwp.carers.s2.xml.validation.XmlValidator
import play.api.Logger
import xml.DWPBody


class XmlSubmitter extends Submitter {
  val transactionID = "TEST432"

  override def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
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