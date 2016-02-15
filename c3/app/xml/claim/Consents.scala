package xml.claim

import models.domain.Claim
import models.yesNo.YesNoWithText
import xml.XMLHelper._
import xml.XMLComponent

import scala.xml.NodeSeq

object Consents extends XMLComponent {

  def xml(claim: Claim) = {
    val consent = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Consents>
      {questionWhy(<Consent/>,"tellUsWhyFromAnyoneOnForm.informationFromPerson", consent.informationFromPerson.answer, consent.informationFromPerson.text, "tellUsWhyFromAnyoneOnForm.whyPerson")}
    </Consents>
  }

  def fromXml(xml: NodeSeq, claim: Claim) : Claim = {
    val consents = (xml \\ "Consents")
    val declaration = models.domain.Declaration(informationFromPerson = YesNoWithText(answer = createYesNoText((consents \ "Consent" \ "Answer").text), text = createStringOptional((consents \ "Consent" \ "Why" \ "Answer").text)))
    claim.update(declaration)
  }
}
