package controllers.submission

import app.ConfigProperties._
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, Request}
import play.api.Logger
import xml.DWPBody
import models.view.CachedClaim._
import models.domain.Claim

object XmlSubmitter {
  val transactionID = "TEST432"

  def submission(claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val validator = xmlValidator(claim)
    val fullXml = DWPBody().xml(claim,transactionID)

    if (getProperty("validateXml",default=true)) {
      val fullXmlString = fullXml.buildString(stripComments = true)
      Logger.debug(s"generate xml : ${claim.key}")

      validator.validate(fullXmlString) match {
        case true => claim -> Ok(fullXml.buildString(stripComments = false))
        case false => claim -> Ok("Failed validation")
      }
    } else {
     claim -> Ok(fullXml.buildString(stripComments = false))
    }
  }
}