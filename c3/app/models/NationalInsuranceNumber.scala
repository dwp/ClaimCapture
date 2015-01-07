package models

case class NationalInsuranceNumber(ni1: Option[String]) {
  def stringify = if (ni1.isDefined) ni1.get.toUpperCase
                  else ""
}