package xml

import models.domain.Claim
import scala.xml.Elem

/**
 * TODO write description
 * @author Jorge Migueis
 */
trait XMLBuilder {

  def xml(claim: Claim, transactionId : String):Elem

}