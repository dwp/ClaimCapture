package controllers.submission

import app.ConfigProperties._
import play.api.mvc.Results.Ok
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{SimpleResult, AnyContent, Request}
import ExecutionContext.Implicits.global
import scala.xml.Elem
import com.dwp.carers.s2.xml.validation.XmlValidator
import play.api.Logger
import models.domain.Claim
import xml.DWPBody

object XmlSubmitter {
  val transactionID = "TEST432"

  def submission(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = {
    val validator = xmlValidator(claim)
    val fullXml = DWPBody().xml(claim,transactionID)

    if (getProperty("validateXml",default=true)) {
      val fullXmlString = fullXml.buildString(stripComments = true)
      Logger.debug(s"generate xml : ${claim.key}")

      validator.validate(fullXmlString) match {
        case true => Future(Ok(fullXml.buildString(stripComments = false)))
        case false => Future(Ok("Failed validation"))
      }
    } else {
      Future(Ok(fullXml.buildString(stripComments = false)))
    }
  }
}