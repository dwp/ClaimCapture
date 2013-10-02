package jmx

import java.lang.management.ManagementFactory
import javax.management.modelmbean.{RequiredModelMBean, ModelMBeanInfoSupport, ModelMBeanOperationInfo}
import javax.management.ObjectName
import scala.reflect.runtime.{universe => ru}

trait MBean {
  self =>

  MBeanRegistry.register(self)
}

object MBeanRegistry {
  val mbs = ManagementFactory.getPlatformMBeanServer

  def register(mbean: MBean) = {
    val rm = ru.runtimeMirror(getClass.getClassLoader)
    val mType = rm.classSymbol(mbean.getClass).selfType
    var ops = List[ModelMBeanOperationInfo]()

    mType.declarations.foreach(symbol => {
      symbol.annotations.find(a => a.tpe == ru.typeOf[MOperation]) match {
        case Some(_) => {
          import language.reflectiveCalls
          val cmx = rm.asInstanceOf[{ def methodToJava(sym: scala.reflect.internal.Symbols#MethodSymbol): java.lang.reflect.Method }]
          val jm = cmx.methodToJava(symbol.asMethod.asInstanceOf[scala.reflect.internal.Symbols#MethodSymbol])
          ops = ops :+ new ModelMBeanOperationInfo(jm.toString, jm)
        }
        case None =>
      }
    })

    val mbeansOps = ops.toArray
    val modelMBeanInfoSupport = new ModelMBeanInfoSupport(getClass.getName, mbean + " Model MBean", null, null, mbeansOps, null)
    val requiredModelMBean = new RequiredModelMBean(modelMBeanInfoSupport)
    requiredModelMBean.setManagedResource(mbean, "ObjectReference")
    val mBeanServer = ManagementFactory.getPlatformMBeanServer
    mBeanServer.registerMBean(requiredModelMBean, new ObjectName("c3.claim:type=" + mbean))
  }
}