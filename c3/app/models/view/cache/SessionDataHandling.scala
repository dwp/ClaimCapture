package models.view.cache

import java.util.concurrent.TimeUnit
import javax.xml.bind.DatatypeConverter

import app.ConfigProperties._
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.StaxDriver
import models.domain.{Iteration, Section, QuestionGroup, Claim}
import models.view.cache.HttpUtils.HttpMethodWrapper
import org.json4s.native.JsonMethods._
import play.api.Logger
import play.api.libs.ws.{WSResponse, WS}
import play.api.http.Status
import play.api.Play.current
import services.XStreamConversions
import utils.XorEncryption

import scala.language.implicitConversions

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import scala.xml.Elem

import org.json4s._
import org.json4s.jackson.Serialization

/**
  * Created by peterwhitehead on 17/11/2016.
  */
trait SessionDataHandling {
  implicit def stringWrapper(s:String): HttpMethodWrapper = new HttpMethodWrapper(s, FiniteDuration(getIntProperty("sd.timeout"), TimeUnit.MILLISECONDS))
  implicit val formats = Serialization.formats(FullTypeHints(List(classOf[QuestionGroup], classOf[Section], classOf[Iteration])))

  def sdUrl = getStringProperty("sd.url")
  def sdKey = getStringProperty("session.data.uuid.secret.key")

  def loadString(key: String): String = {
    Logger.info(s"Retrieving claim data from session database for key:$key")
    s"$sdUrl/session/load/${XorEncryption.encryptUuid(key, sdKey)}" post { response: WSResponse =>
        response.status match {
          case Status.OK => response.body
          case status@_ => {
            Logger.debug(s"Failed to load claim from session database for key:$key status:${response.status}")
            ""
          }
        }
      } exec()
  }

  def load(key: String) : Option[Claim] = {
    Logger.info(s"Loading claim from session database for key:$key")
    loadString(key) match {
      case claimString if !claimString.isEmpty => parseClaim(claimString)
      case _ => {
        Logger.debug(s"No data in session database for key:$key")
        None
      }
    }
  }

  def save(key: String, claim: Claim): Unit = {
    Logger.info(s"Saving claim into session database for key:$key")

    val xml = XStreamConversions(new XStream(new StaxDriver())).toXML(claim)

    s"$sdUrl/session/save/${XorEncryption.encryptUuid(key, sdKey)}" post { response: WSResponse =>
      response.status match {
        case _ => {
          Logger.debug(s"Status for saving claim into session database for key:$key status:${response.status}")
          response.status.toString
        }
      }
    } exec(DatatypeConverter.printBase64Binary(xml.getBytes))
  }

  def delete(key: String) = {
    Logger.info(s"Deleting claim from session database for key:$key")
    s"$sdUrl/session/delete/${XorEncryption.encryptUuid(key, sdKey)}" delete { response: WSResponse =>
      response.status match {
        case _ => {
          Logger.debug(s"Status for deleting claim from session database for key:$key status:${response.status}")
          response.status.toString
        }
      }
    }
  }

  private def parseClaim(claimString: String): Option[Claim] = {
    Logger.info(s"Creating claim from data returned")
    val sessionData = parse(claimString).extract[SessionData]

    if (sessionData.payload == null) {
      Logger.info(s"Failed to load claim from session database.")
      None
    } else if (sessionData.minutesSinceLastActive != null && sessionData.minutesSinceLastActive.toInt > getIntProperty("session.data.claim.expiry")) {
      Logger.info(s"Claim expired in session database.")
      None
    } else {
      extractClaim(sessionData.payload)
    }
  }

  private def extractClaim(payload: String): Option[Claim] = {
    val claim = new Claim(key="claim")
    Logger.info(s"Extracting claim data from session.")
    val claimString = new String(DatatypeConverter.parseBase64Binary(payload))
    XStreamConversions(new XStream(new StaxDriver())).fromXML(claimString, claim)
    Logger.info(s"Created claim from session data.")
    Some(claim)
  }
}

object HttpUtils {
  implicit val context = scala.concurrent.ExecutionContext.Implicits.global

  class HttpMethodWrapper(url:String, timeout:FiniteDuration){
    def get[T](m:WSResponse => T):T = Await.result(WS.url(url).get().map(m),timeout)

    def delete[T](m:WSResponse => T):T = Await.result(WS.url(url).delete().map(m),timeout)

    def put[T](m:WSResponse => T) = new {
      def exec(s:String):T = Await.result(WS.url(url).put(s).map(m),timeout)
      def exec(map:Map[String,Seq[String]] = Map.empty[String,Seq[String]]):T = Await.result(WS.url(url).put(map).map(m),timeout)
    }

    def post[T](m:WSResponse => T) = new {
      def exec(map:Map[String,Seq[String]] = Map.empty[String,Seq[String]]):T = Await.result(WS.url(url).post(map).map(m),timeout)
      def exec(s:String):T = Await.result(WS.url(url).post(s).map(m),timeout)
      def exec(s:Elem):T = Await.result(WS.url(url).post(s).map(m),timeout)
    }
  }
}

case class SessionData(sessionId: String, payload: String, minutesSinceLastActive: String)
