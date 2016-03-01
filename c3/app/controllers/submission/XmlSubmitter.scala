package controllers.submission

import app.ConfigProperties._
import models.view.ClaimHandling.ClaimResult
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, Request}
import xml.DWPBody
import models.domain.Claim
import play.api.Logger
import scala.collection.JavaConverters._

object XmlSubmitter {
  val transactionID = "16010000027"

  def submission(claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val validator = xmlValidator(claim)
    val fullXml = DWPBody().xml(claim,transactionID)

    if (getProperty("validateXml", default = true)) {
      val xmlErrors = validator.validate(fullXml.mkString)
      xmlErrors.hasFoundErrorOrWarning match {
        case true => claim -> {
          xmlErrors.getWarningAndErrors.asScala.foreach(error => Logger.error(s"Validation error: $error"))
          Logger.error(s"Failed validating claim: ${fullXml.mkString}")
          Ok("Failed validation")
        }
        case false => claim -> Ok(fullXml)
      }
    } else {
     // TODO println(fullXml) -
     claim -> Ok(fullXml)
    }
  }
}
