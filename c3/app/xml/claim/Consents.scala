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
      {
        consent.informationFromEmployer.answer match {
          case Some(answer) =>
            <Consent>
              {questionWhy("gettingInformationFromAnyEmployer.informationFromEmployer", titleCase(answer), consent.informationFromEmployer.text)}
            </Consent>
          case _ => NodeSeq.Empty
        }
      }

      {
        consent.informationFromPerson.answer.isEmpty match {
          case false =>
          <Consent>
            {questionWhy("tellUsWhyEmployer.informationFromPerson", titleCase(consent.informationFromPerson.answer), consent.informationFromPerson.text)}
          </Consent>
          case _ => NodeSeq.Empty
        }
      }

    </Consents>

  }
}
