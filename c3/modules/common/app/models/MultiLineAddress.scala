package models

case class MultiLineAddress(street: Street = Street(), town: Option[Town] = None){
  def lineOne() = street.lineOne
  def lineTwo() = town.getOrElse(Town()).lineTwo
  def lineThree() = town.getOrElse(Town()).lineThree
}

case class Street(lineOne: Option[String] = None)
case class Town(lineTwo: Option[String] = None, lineThree: Option[String] = None)