package models

case class NationalInsuranceNumber(nino: Option[String]) {
  def stringify = if (nino.isDefined) nino.get.toUpperCase
                  else ""
}
