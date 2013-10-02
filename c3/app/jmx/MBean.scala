package jmx

import java.lang.management.ManagementFactory
import javax.management.ObjectName
import play.api.Logger

trait MBean {
  self =>

  MBeanRegistry.register(self)

  def name: String
}

object MBeanRegistry {
  val mbeanServer = ManagementFactory.getPlatformMBeanServer

  def register(mbean: MBean) = {
    Logger.info("Registering mbean " + mbean.name)
    mbeanServer.registerMBean(mbean, new ObjectName(mbean.name))
  }
}