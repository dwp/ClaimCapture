package xml

import models.domain.Claim
import scala.xml.Elem
import java.text.SimpleDateFormat
import java.util.Date

/**
 * TODO write description
 * @author Jorge Migueis
 */
object DWPBody {
  def xml(claim: Claim, transactionId : String): Elem = {
    <DWPBody xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns="http://www.govtalk.gov.uk/dwp/carers-allowance"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/carers-allowance file:./CarersAllowance_Schema.xsd">
    <Version>0.1</Version>
    <DWPCATransaction>
      <TransactionId>{transactionId}</TransactionId>
      <DateTimeGenerated>{new SimpleDateFormat("dd-MM-YYYY HH:mm").format(new Date())}</DateTimeGenerated>
      {controllers.submission.xmlGenerator(claim)}
    </DWPCATransaction>
    </DWPBody>
  }
}
