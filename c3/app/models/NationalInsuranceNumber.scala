package models

case class NationalInsuranceNumber(ni1: Option[String], ni2: Option[String], ni3: Option[String], ni4: Option[String], ni5: Option[String]) {
  def stringify = if (ni1.isDefined && ni2.isDefined && ni3.isDefined && ni4.isDefined && ni5.isDefined) s"${ni1.get}${ni2.get}${ni3.get}${ni4.get}${ni5.get}".toUpperCase
                  else ""
}