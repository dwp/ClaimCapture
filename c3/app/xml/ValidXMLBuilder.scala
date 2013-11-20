package xml

import models.domain.Claim
import scala.xml.Elem
import play.api.Play

/**
 * Validates the XML built by an underlying XML builder, by default [[xml.DWPBody]].
 * Return the XML if valid, otherwise throws a RuntimeException.
 * Used by [[controllers.submission.WebServiceSubmitter]]
 * @author Jorge Migueis
 */
class ValidXMLBuilder(underlying:XMLBuilder)  extends XMLBuilder {
  def xml(claim: Claim, transactionId: String): Elem = {
    val xmlGenerated = underlying.xml(claim,transactionId)
    val validator = controllers.submission.xmlValidator(claim)
    if (Play.current.configuration.getBoolean("validateXml").orElse(Some(true)).get
      && !validator.validate(xmlGenerated.toString())) throw new RuntimeException("Invalid XML generated. See log file.")
    xmlGenerated
  }
}

object ValidXMLBuilder {
  def apply(underlying:XMLBuilder = DWPBody()) = new ValidXMLBuilder(underlying)
}
