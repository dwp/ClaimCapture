package xml

import app.ConfigProperties._
import models.domain.Claim
import play.api.Logger
import scala.xml.NodeSeq
import scala.collection.JavaConverters._

/**
 * Validates the XML built by an underlying XML builder, by default [[xml.DWPBody]].
 * Return the XML if valid, otherwise throws a RuntimeException.
 * @author Jorge Migueis
 */
class ValidXMLBuilder(underlying:XMLBuilder)  extends XMLBuilder {
  def xml(claim: Claim, transactionId: String): NodeSeq = {
    val xmlGenerated = underlying.xml(claim,transactionId)
    val validator = controllers.submission.xmlValidator(claim)
    if (getBooleanProperty("validateXml")) {
      val xmlErrors = validator.validate(xmlGenerated.toString())
      if (xmlErrors.hasFoundErrorOrWarning) {
        xmlErrors.getWarningAndErrors.asScala.foreach(error => Logger.error(s"Validation error: $error"))
        throw new RuntimeException("Invalid XML generated. See log file.")
      }
    }
    xmlGenerated
  }
}

object ValidXMLBuilder {
  def apply(underlying:XMLBuilder = DWPBody()) = new ValidXMLBuilder(underlying)
}
