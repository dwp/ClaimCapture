package controllers.submission

import app.ConfigProperties._
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, Request}
import xml.DWPBody
import models.view.CachedClaim._
import models.domain.Claim
import play.api.Logger

object XmlSubmitter {
  val transactionID = "1408TEST432"

  def submission(claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val validator = xmlValidator(claim)
    val fullXml = DWPBody().xml(claim,transactionID)

    println(fullXml)

    if (getProperty("validateXml", true)) {
      validator.validate(fullXml.mkString) match {
        case true => claim -> Ok(fullXml)
        case false => claim -> {
          Logger.debug(fullXml.mkString)
          Ok("Failed validation")
        }
      }
    } else {
     // TODO println(fullXml) - use when you want to get test xml for the pdfService functional tests
     claim -> Ok(fullXml)
    }
  }
}