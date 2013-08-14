package controllers.submission

import play.api.mvc.Results.Ok
import models.domain.Claim
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, PlainResult}
import ExecutionContext.Implicits.global
import xml.DWPCAClaim
import scala.xml.Elem
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import play.api.Logger
import play.Configuration

class XmlSubmitter extends Submitter {
  def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
    val claimXml = DWPCAClaim.xml(claim, "TEST432")
    val fullXml = buildFullClaim(claimXml)

    Logger.info(fullXml.buildString(stripComments = true))

    if (Configuration.root().getBoolean("validateXml", true)) {
      val validator = XmlValidatorFactory.buildCaValidator()
      validator.validate(fullXml.buildString(stripComments = true)) match {
        case true => Future(Ok(claimXml.buildString(stripComments = false)))
        case false => Future(Ok("Failed validation"))
      }
    } else {
      Future(Ok(claimXml.buildString(stripComments = false)))
    }
  }

  private def buildFullClaim(claimXml: Elem) = {
    <DWPBody xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666"
             xmlns="http://www.govtalk.gov.uk/dwp/ca/claim"
             xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
             xmlns:dc="http://purl.org/dc/elements/1.1/"
             xmlns:dcq="http://purl.org/dc/terms/"
             xmlns:gms="http://www.govtalk.gov.uk/CM/gms"
             xmlns:dsig="http://www.w3.org/2000/09/xmldsig#"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/ca/claim file:/Users/jmi/Temp/dwp-ca-claim-v1_10.xsd">
      <DWPEnvelope>
        <DWPCAHeader>
          <TestMessage>5</TestMessage>
          <Keys>
            <Key type="}~e"></Key>
            <Key type="Z}"></Key>
          </Keys>
          <Language>en</Language>
          <DefaultCurrency>GBP</DefaultCurrency>
          <Manifest>
            <Reference>
              <Namespace>http://PtqKCMVh/</Namespace>
              <SchemaVersion></SchemaVersion>
              <TopElementName>FZXic.rwPpxsw5wsX</TopElementName>
            </Reference>
            <Reference>
              <Namespace>http://jwJGvJlj/</Namespace>
              <SchemaVersion></SchemaVersion>
              <TopElementName>vaN1Eh5z61pekYlfOv-vP0sGy</TopElementName>
            </Reference>
          </Manifest>
          <TransactionId>
            {(claimXml \\ "DWPCAClaim" \ "@id").text}
          </TransactionId>
        </DWPCAHeader>{claimXml}
      </DWPEnvelope>
    </DWPBody>
  }
}
