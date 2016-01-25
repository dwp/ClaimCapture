package controllers.submission

import app.ConfigProperties._
import models.view.ClaimHandling.ClaimResult
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, Request}
import xml.DWPBody
import models.domain.Claim
import play.api.Logger

object XmlSubmitter {
  val transactionID = "16010000027"

  def submission(claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val validator = xmlValidator(claim)
    val fullXml = DWPBody().xml(claim,transactionID)

    if (getProperty("validateXml", default = true)) {
      validator.validate(fullXml.mkString) match {
        case true => claim -> Ok(fullXml)
        case false => claim -> {
          Logger.error(s"Failed validating claim: ${fullXml.mkString}")
          Ok("Failed validation")
        }
      }
    } else {
     // TODO println(fullXml) -
     claim -> Ok(fullXml)
    }
  }
}
