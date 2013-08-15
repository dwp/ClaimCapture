package models

case class Whereabouts(location: String = "", other: Option[String] = None)
case class PaymentFrequency(frequency:String,other: Option[String]) {
  def stringify = frequency +  " " + other.getOrElse("")
}