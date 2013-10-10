package jmx.inspectors

import scala.util.Try
import akka.actor.Actor
import net.sf.ehcache.CacheManager
import jmx.MBean

trait PlayMBean extends MBean {
  def jmxEnabled():Boolean = play.Configuration.root().getBoolean("jmxEnabled",false)
}

trait ApplicationInspectorMBean extends MBean {
  override def name = "c3:name=Application"

  def getSessionCount: Int

  def getRefererRedirects: Int
}

class ApplicationInspector() extends Actor with ApplicationInspectorMBean with PlayMBean {
  var sessionCount: Int = 0

  var refererRedirects: Int = 0

  override def getSessionCount: Int = {
    sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
    sessionCount
  }

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