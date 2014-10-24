package xml

import app.ConfigProperties._
import models.domain.Claim

import scala.xml.NodeSeq

/**
 * Validates the XML built by an underlying XML builder, by default [[xml.DWPBody]].
 * Return the XML if valid, otherwise throws a RuntimeException.
 * @author Jorge Migueis
 */
class ValidXMLBuilder(underlying:XMLBuilder)  extends XMLBuilder {
  def xml(claim: Claim, transactionId: String): NodeSeq = {
    val xmlGenerated = underlying.xml(claim,transactionId)
    val validator = controllers.submission.xmlValidator(claim)
    if (getProperty("validateXml",default=false)
      && !validator.validate(xmlGenerated.toString())) throw new RuntimeException("Invalid XML generated. See log file.")
    xmlGenerated
  }
}

object ValidXMLBuilder {
  def apply(underlying:XMLBuilder = DWPBody()) = new ValidXMLBuilder(underlying)
}
