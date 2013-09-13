package xml

import models.domain.Circs
import scala.xml.Elem
import play.api.Logger

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 11/09/2013
 */
object DWPCoCircs {

  def xml(circs: Circs, transactionId : String):Elem = {
    Logger.info(s"Build DWPCoCircs : $transactionId")
    <DWPCAChangeOfCircumstances id={transactionId}>
      {Identification.xml(circs)}
      {AdditionalInfo.xml(circs)}
    </DWPCAChangeOfCircumstances>
  }

}
