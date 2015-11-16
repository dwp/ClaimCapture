package xml

import models.domain.Claim

import scala.xml.NodeSeq

/**
 * Interface of the classes/Objects that build a full XML.
 * @author Jorge Migueis
 */
trait XMLBuilder {

  def xml(claim: Claim, transactionId : String):NodeSeq

}
