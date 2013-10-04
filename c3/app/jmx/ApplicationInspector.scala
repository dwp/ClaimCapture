package jmx

import scala.util.Try
import akka.actor.Actor
import net.sf.ehcache.CacheManager
import jmx.JMXActors._

trait ApplicationInspectorMBean extends MBean {
  override def name = "c3:name=Application"

  def getSessionCount: Int

  def setSessionCount(i: Int): Unit

  def getRefererRedirects: Int
}

class ApplicationInspector() extends Actor with ApplicationInspectorMBean {
  var sessionCount: Int = 0

  var refererRedirects: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

  override def setSessionCount(i: Int) = sessionCount = i

  override def getRefererRedirects = refererRedirects

  def receive = {
    case GetSessionCount =>
      sender ! getSessionCount

    case RefererRedirect =>
      refererRedirects = refererRedirects + 1

    case GetRefererRedirects =>
      sender ! getRefererRedirects
  }
}

trait RefererFilterNotifier {
  def fireNotification[R](proceed: => R) = {
    applicationInspector ! RefererRedirect
    proceed
  }
}