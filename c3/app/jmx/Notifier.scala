package jmx

import jmx.JMXActors._

trait RefererFilterNotifier {
  def fireNotification[R](proceed: => R = Unit) = {
    applicationInspector ! RefererRedirect
    proceed
  }
}