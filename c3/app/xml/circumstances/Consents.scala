package xml.circumstances

import xml.XMLComponent
import models.domain.{CircumstancesDeclaration, Claim}
import scala.xml.NodeSeq
import xml.XMLHelper._

/**
 * @author Jorge Migueis
 */
object Consents extends XMLComponent{
  def xml(circs: Claim): NodeSeq = {

    val consent = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    <Consents>
      {questionWhy(<Consent/>,"obtainInfoAgreement",consent.obtainInfoAgreement,consent.obtainInfoWhy, "obtainInfoWhy")}
    </Consents>
  }
}
