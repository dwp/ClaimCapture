package xml

import models.domain.Claim
import scala.xml.{NodeSeq, Elem}
import play.api.i18n.Messages
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

/**
 * Interface of objects that generates parts of the XML
 * that will compose the full XML to submit to ingress and pdf services.
 * @author Jorge Migueis
 */
trait XMLComponent {

  def xml(claim: Claim): NodeSeq
}
