package models

case class MultiLineAddress(lineOne: Option[String] = None, lineTwo: Option[String] = None, lineThree: Option[String] = None){
  def numberOfCharactersInput: Int = {
    {lineOne match {case Some(s) => s.length case _ => 0}} +
    {lineTwo match {case Some(s) => s.length case _ => 0}} +
    {lineThree match {case Some(s) => s.length case _ => 0}}
  }
}