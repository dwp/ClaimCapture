package jmx

import scala.collection.mutable.ListBuffer
import akka.actor.Actor
import play.api.mvc.RequestHeader
import org.joda.time.DateTime
import java.util

case class RequestDetails(header: RequestHeader, requestDateTime: DateTime = DateTime.now(), resultDateTime: DateTime = DateTime.now())

case object GetRequestDetails

class RequestMonitor extends Actor with MBean {
  val requestDetails = ListBuffer[RequestDetails]()

  import scala.collection.JavaConverters._

  @MOperation def requestDetailsMap = new util.HashMap[String, String](requestDetails.map(r => (r.header.path, s"Start: ${r.requestDateTime}, End: ${r.resultDateTime}")).toMap.asJava)

  @MOperation def requestDetailsList = new util.ArrayList[String](requestDetails.map(r => s"${r.header.path}, Start: ${r.requestDateTime}, End: ${r.resultDateTime}").asJava)

  def receive = {
    case r: RequestDetails => requestDetails += r
    case GetRequestDetails => sender ! requestDetails.toList
  }
}