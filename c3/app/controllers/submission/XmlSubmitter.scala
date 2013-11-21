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



class XmlSubmitter extends Submitter {
  val transactionID = "TEST432"

  override def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
    val (xml, validator) = xmlAndValidator(claim, transactionID)

    if (Configuration.root().getBoolean("validateXml", true)) {
      val fullXml = buildFullClaim(xmlValidator(claim), xml)
      val fullXmlString = fullXml.buildString(stripComments = true)
      Logger.debug(s"generate xml : ${claim.key}")

      validator.validate(fullXmlString) match {
        case true => Future(Ok(xml.buildString(stripComments = false)))
        case false => Future(Ok("Failed validation"))
      }
    } else {
      Future(Ok(xml.buildString(stripComments = false)))
    }
  }

  def buildFullClaim(validator: XmlValidator, claimXML: Elem) =
      <DWPBody  xmlns="http://www.govtalk.gov.uk/dwp/carers-allowance"
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation={validator.getSchemaLocation}>

     <Version>0.9</Version>
      <DWPCATransaction>
        <TransactionId>{transactionID}</TransactionId>
        <DateTimeGenerated>02-10-2010 14:36</DateTimeGenerated>
        {claimXML}
      </DWPCATransaction>
      {Signature.signatureXml}
    </DWPBody>
}