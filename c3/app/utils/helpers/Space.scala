package utils.helpers

import scala.util.Random

object Space {
   def * = {
     " " * new Random().nextInt(10)
   }
}
