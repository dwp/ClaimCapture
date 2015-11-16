package xml.claim

import models.domain.Claim
import xml.XMLHelper._
import xml.XMLComponent
import play.api.i18n.Messages
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

object Consents extends XMLComponent {

  def xml(claim: Claim) = {
    val consent = claim.questionGroup[models.domain.Declaration].getOrElse(models.domain.Declaration())

    <Consents>
      {questionWhy(<Consent/>,"tellUsWhyFromAnyoneOnForm_informationFromPerson", consent.informationFromPerson.answer, consent.informationFromPerson.text, "tellUsWhyFromAnyoneOnForm_whyPerson")}
    </Consents>
  }
}
