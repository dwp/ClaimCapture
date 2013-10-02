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
  val mbs = ManagementFactory.getPlatformMBeanServer

  val mBeanServer = ManagementFactory.getPlatformMBeanServer

  def register(mbean: MBean) = {
    Logger.info("Registering bean " + mbean.name)
    mBeanServer.registerMBean(mbean, new ObjectName(mbean.name))
  }
}