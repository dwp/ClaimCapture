package utils.helpers

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 30/08/2013
 */
object StringPadding {

  def leftPad(totalLength:Int, stringToPad:String, paddingCharacter:Char):String = {
     val times = totalLength - stringToPad.length
     if (times > 0) s"$paddingCharacter" * times + stringToPad else stringToPad
  }

  def leftPadWithZero(totalLength:Int, stringToPad:String) = leftPad(totalLength,stringToPad,'0')

}
