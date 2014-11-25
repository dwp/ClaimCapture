package xml

import java.text.SimpleDateFormat
import java.util.Date

import com.dwp.carers.s2.xml.signing.XmlSignatureFactory
import controllers.submission.xmlValidator
import models.domain.Claim
import play.api.i18n.Lang

import scala.xml.{Elem, NodeSeq, XML}

/**
 * Generates the full XML, including the digital signature.
 * It is a class to make it testable.
 * @author Jorge Migueis
 */
 class DWPBody extends XMLBuilder {
  def xml(claim: Claim, transactionId : String):NodeSeq = {
    NodeSeq.Empty ++
      signDwpClaim(<DWPBody xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns="http://www.govtalk.gov.uk/dwp/carers-allowance"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation={xmlValidator(claim).getSchemaLocation}>
    <Version>0.5</Version>
    <DWPCATransaction id={transactionId}>
      <TransactionId>{transactionId}</TransactionId>
      <DateTimeGenerated>{new SimpleDateFormat("dd-MM-YYYY HH:mm").format(new Date())}</DateTimeGenerated>
      <LanguageUsed>{claim.lang.getOrElse(Lang("en")).code match {
        case "en" => "English"
        case "cy" => "Welsh"
        case _ => "Unknown"
      } }</LanguageUsed>
      {coreXml(claim)}
      </DWPCATransaction>
    </DWPBody>,transactionId)
  }

  /**
   * Signed the XML provided as a sequence of nodes according to XML security standard.
   * @param dwpClaim XML to sign
   * @param transactionId transaction id used to indicate which section of the XML to sign (i.e. DWPCATransaction)
   * @return  XML signed
   */
  private def signDwpClaim(dwpClaim: Elem,transactionId: String): Elem = {
    val signatory = XmlSignatureFactory.buildDsaSha1Generator()
    val xmlStringSigned = signatory.sign(dwpClaim.buildString(stripComments = true),s"$transactionId")
    XML.loadString(xmlStringSigned)
  }

  protected def coreXml(claim:Claim) = controllers.submission.xmlGenerator(claim)

}

object DWPBody {
  def apply() = new DWPBody
}
