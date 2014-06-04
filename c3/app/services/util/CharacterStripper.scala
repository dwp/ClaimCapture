package services.util

object CharacterStripper {

  def strip(stringToStrip:String, charsToStrip:String) = {
    stringToStrip.filterNot(charsToStrip.toSet)
  }
}
