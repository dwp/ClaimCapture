package xml.claim

import app.XMLValues
import models.domain.{Consent, Claim}
import xml.XMLHelper._
import scala.Some
import scala.xml.NodeSeq
import play.api.i18n.Messages


object Consents {

  def xml(claim: Claim) = {

    val consent = claim.questionGroup[Consent].getOrElse(Consent())

    <Consents>
      {
        consent.informationFromEmployer.answer match {
          case Some(answer) =>
            <Consent>
              <QuestionLabel>{Messages("gettingInformationFromAnyEmployer.informationFromEmployer")}</QuestionLabel>
              <Answer>{titleCase(answer)}</Answer>
              {
                 titleCase(answer) match{
                  case "No" => <Why>{ consent.informationFromEmployer.text.orNull }</Why>
                  case _ =>
                }
              }
            </Consent>
          case _ => NodeSeq.Empty
        }
      }

      {
        consent.informationFromPerson.answer.isEmpty match {
          case false =>
          <Consent>
            <QuestionLabel>{Messages("tellUsWhyEmployer.informationFromPerson")}</QuestionLabel>
            <Answer>{titleCase(consent.informationFromPerson.answer)}</Answer>
            {
              titleCase(consent.informationFromPerson.answer) match{
              case "No" => <Why>{ consent.informationFromPerson.text.orNull }</Why>
              case _ =>
              }
            }
          </Consent>
          case _ => NodeSeq.Empty
        }
      }

    </Consents>

  }
}
