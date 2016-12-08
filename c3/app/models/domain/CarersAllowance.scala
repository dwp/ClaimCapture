package models.domain

import app.ConfigProperties._

object CarersAllowance extends Identifier(id = "s0")

case class Benefits(benefitsAnswer: String = "") extends QuestionGroup(Benefits) with BooleanConfirmation
{
  val answer: Boolean = benefitsAnswer match {
    case Benefits.noneOfTheBenefits => false
    case _ => true
  }
}

object Benefits extends QGIdentifier(id = s"${CarersAllowance.id}.g1") {

  val pip = "PIP"
  val dla = "DLA"
  val aa = "AA"
  val caa = "CAA"
  val afip = "AFIP"
  val noneOfTheBenefits = "NONE" // None of the benefits
}

case class Eligibility( hours: String = "",
                        over16: String = "",
                        origin: String = "") extends QuestionGroup(Eligibility) {
  // livesInGB Yes/No actually reflects whether gbsite-living-in-gb or nisite-living-in-ni.
  val livesInGB: String = {
    ( getStringProperty("origin.tag"), origin ) match{
      case ( "GB", "GB" ) => "yes"
      case ( "GB-NIR", "NI" ) => "yes"
      case _ => "no"
    }
  }
}

object Eligibility extends QGIdentifier(id = s"${CarersAllowance.id}.g2")

case class ProceedAnyway(allowedToContinue: Boolean, answerYesNo: Option[String] = None, jsEnabled: Boolean = false) extends QuestionGroup(ProceedAnyway) with BooleanConfirmation {
  val answer: Boolean = allowedToContinue || (answerYesNo match {
    case Some(answerYN) if answerYN == "yes" => true
    case _ => false
  })
}

object ProceedAnyway extends QGIdentifier(id = s"${CarersAllowance.id}.g6")

