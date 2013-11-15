package xml

import models.domain.Claim
import scala.xml.{XML, NodeSeq, Elem}
import java.text.SimpleDateFormat
import java.util.Date
import com.dwp.carers.s2.xml.signing.XmlSignatureFactory

/**
 * Generates the full XML, including the digital signature.
 * It is a class to make it testable.
 * @author Jorge Migueis
 */
 class DWPBody extends XMLBuilder {
  def xml(claim: Claim, transactionId : String): Elem = {
    signDwpClaim(<DWPBody xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns="http://www.govtalk.gov.uk/dwp/carers-allowance"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/carers-allowance file:./CarersAllowance_Schema.xsd">
    <Version>0.1</Version>
    <DWPCATransaction>
      <TransactionId>{transactionId}</TransactionId>
      <DateTimeGenerated>{new SimpleDateFormat("dd-MM-YYYY HH:mm").format(new Date())}</DateTimeGenerated>
      {coreXml(claim)}
      </DWPCATransaction>
    </DWPBody>)
  }

  /**
   * Signed the XML provided as a sequence of nodes according to XML security standard.
   * @param dwpClaim XML to sign
   * @return  XML signed
   */
  private def signDwpClaim(dwpClaim: Elem): Elem = {
    val signatory = XmlSignatureFactory.buildDsaSha1Generator()
    val xmlStringSigned = signatory.sign(dwpClaim.buildString(stripComments = true))
    XML.loadString(xmlStringSigned)
  }

  protected def coreXml(claim:Claim) = controllers.submission.xmlGenerator(claim)

}

object DWPBody {
  def apply() = new DWPBody
}
