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
    val mbeanObjectName = new ObjectName(mbean.name)

    if (mbeanServer.isRegistered(mbeanObjectName)) {
      mbeanServer.getObjectInstance(mbeanObjectName)
    } else {
      Logger.info("Registering mbean " + mbean.name)
      mbeanServer.registerMBean(mbean, mbeanObjectName)
    }
  }
}