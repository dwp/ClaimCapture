package implicits

import language.reflectiveCalls
import language.implicitConversions

object Iteration {
  implicit def iterate(i: Int) = new {
    def x(f: => Unit) = for (i <- 1 to i) f
  }
}
