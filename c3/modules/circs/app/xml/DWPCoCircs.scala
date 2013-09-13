package xml

import models.domain.Circs
import scala.xml.Elem

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 11/09/2013
 */
object DWPCoCircs {

  def xml(circs: Circs, transactionId : String):Elem = {
    <DWPCAClaim id={transactionId}>
      {Claimant.xml(circs)}
    </DWPCAClaim>
  }

}
