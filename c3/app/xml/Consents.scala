package xml

import app.XMLValues
import models.domain.{Consent, Claim}
import xml.XMLHelper._
import scala.Some
import scala.xml.NodeSeq


object Consents {

  def xml(claim: Claim) = {

    val consent = claim.questionGroup[Consent].getOrElse(Consent())

    <Consents>
      {
        consent.informationFromEmployer.answer match {
          case Some(answer) =>
            <Consent>
              <QuestionLabel>Do you agree to us getting information from any current or previous employer you have told us about as part of this claim? </QuestionLabel>
              <Answer>{titleCase(answer)}</Answer>
              {titleCase(answer) match{
                case "No" => "If you answered No please tell us why " + { consent.informationFromEmployer.text.orNull }
                case _ =>
              }}
            </Consent>
          case _ => NodeSeq.Empty
        }
      }

      {
        consent.informationFromPerson.answer.isEmpty match {
          case false =>
          <Consent>
            <QuestionLabel>Do you agree to us getting information from any other person or organisation you have told us about as part of this claim? </QuestionLabel>
            <Answer>{titleCase(consent.informationFromPerson.answer)}</Answer>
            {titleCase(consent.informationFromPerson.answer) match{
            case "No" => "If you answered No please tell us why " + { consent.informationFromPerson.text.orNull }
            case _ =>
          }}
          </Consent>
          case _ => NodeSeq.Empty
        }
      }

    </Consents>

  }
}
