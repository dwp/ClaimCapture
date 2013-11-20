package xml

import models.domain.Claim
import scala.xml.Elem

/**
 * Interface of the classes/Objects that build a full XML.
 * @author Jorge Migueis
 */
trait XMLBuilder {

  def xml(claim: Claim, transactionId : String):Elem

}