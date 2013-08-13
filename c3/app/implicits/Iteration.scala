package implicits

import language.reflectiveCalls
import language.implicitConversions

object Iteration {
  implicit def iterate(i: Int) = {
    import language.reflectiveCalls
    import language.implicitConversions

    new {
      def times(f: => Unit) = for (i <- 1 to i) f
    }
  }
}