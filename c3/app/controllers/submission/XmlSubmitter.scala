package controllers.submission

import play.api.mvc.Results.Ok
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, PlainResult}
import ExecutionContext.Implicits.global
import scala.xml.Elem
import play.Configuration
import models.domain.Claim
import com.dwp.carers.s2.xml.validation.XmlValidator

class XmlSubmitter extends Submitter {
  val transactionID = "TEST432"

  override def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
    val (xml, validator) = xmlAndValidator(claim, transactionID)

    if (Configuration.root().getBoolean("validateXml", true)) {
      val fullXml = buildFullClaim(xmlValidator(claim), xml)
      val fullXmlString = fullXml.buildString(stripComments = true)

      validator.validate(fullXmlString) match {
        case true => Future(Ok(xml.buildString(stripComments = false)))
        case false => Future(Ok("Failed validation"))
      }
    } else {
      Future(Ok(xml.buildString(stripComments = false)))
    }
  }

  def buildFullClaim(validator: XmlValidator, claimXML: Elem) =
    <DWPBody xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666"
             xmlns={validator.getGlobalXmlns}
             xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
             xmlns:dc="http://purl.org/dc/elements/1.1/"
             xmlns:dcq="http://purl.org/dc/terms/"
             xmlns:gms="http://www.govtalk.gov.uk/CM/gms"
             xmlns:dsig="http://www.w3.org/2000/09/xmldsig#"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation={validator.getSchemaLocation}>
      <DWPCATransaction>
        <TransactionId>{transactionID}</TransactionId>
        {claimXML}
      </DWPCATransaction>
    </DWPBody>
}