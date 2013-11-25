package xml.circumstances

import xml.XMLComponent
import models.domain.{CircumstancesDeclaration, Claim}
import scala.xml.NodeSeq
import play.api.i18n.Messages
import xml.XMLHelper._

/**
 * @author Jorge Migueis
 */
object Consents extends XMLComponent{
  def xml(circs: Claim): NodeSeq = {

    val consent = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    <Consents>
      <Consent>
        {questionWhy("obtainInfoAgreement",consent.obtainInfoAgreement,consent.obtainInfoWhy)}
      </Consent>
    </Consents>
  }
}
