package xml.claim

import models.domain.{Consent, Claim}
import xml.XMLHelper._
import scala.Some
import scala.xml.NodeSeq
import play.api.i18n.Messages
import xml.XMLComponent


object Consents extends XMLComponent {

  def xml(claim: Claim) = {

    val consent = claim.questionGroup[Consent].getOrElse(Consent())

    <Consents>
      {questionWhy(<Consent/>,"gettingInformationFromAnyEmployer.informationFromEmployer", consent.informationFromEmployer.answer, consent.informationFromEmployer.text)}
      {questionWhy(<Consent/>,"tellUsWhyEmployer.informationFromPerson", consent.informationFromPerson.answer, consent.informationFromPerson.text)}
    </Consents>
  }
}
