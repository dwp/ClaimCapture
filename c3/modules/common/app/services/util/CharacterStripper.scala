package services.util

object CharacterStripper {

  def strip(stringToStrip:String, charsToStrip:String) = {
    stringToStrip.filterNot(charsToStrip.toSet)
  }

  def stripNonPdf(stringToStrip:String) = {
    strip(stringToStrip, "£()@") // dodgy pdf chars
  }
}
