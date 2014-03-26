package xml

import models.domain.Claim
import scala.xml.{XML, Elem}
import java.text.SimpleDateFormat
import java.util.Date
import com.dwp.carers.s2.xml.signing.XmlSignatureFactory
import controllers.submission.xmlValidator
import play.api.Logger

/**
 * Generates the full XML, including the digital signature.
 * It is a class to make it testable.
 * @author Jorge Migueis
 */
 class DWPBody extends XMLBuilder {
  def xml(claim: Claim, transactionId : String): Elem = {
    <DWPBody xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666"
                          xmlns={xmlValidator(claim).getGlobalXmlns}
                          xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
                          xmlns:dc="http://purl.org/dc/elements/1.1/"
                          xmlns:dcq="http://purl.org/dc/terms/"
                          xmlns:gms="http://www.govtalk.gov.uk/CM/gms"
                          xmlns:dsig="http://www.w3.org/2000/09/xmldsig#"
                          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                          xsi:schemaLocation={xmlValidator(claim).getSchemaLocation}>
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
          <TransactionId>{transactionId}</TransactionId>
        </DWPCAHeader>{coreXml(claim,Some(transactionId))}
      </DWPEnvelope>
    </DWPBody>

  }

  protected def coreXml(claim:Claim,transactionId:Option[String]) = controllers.submission.xmlGenerator(claim,transactionId)

}

object DWPBody {
  def apply() = new DWPBody
}
