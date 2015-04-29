package controllers

import java.io.DataOutputStream
import java.net.{HttpURLConnection, URL}
import java.nio.charset.Charset

import play.api.Logger
import play.api.mvc.{Action, Controller}
import app.ConfigProperties._
import java.util.UUID._

import scala.io.Source
import scala.util.{Failure, Success, Try}

object ChannelShiftParams {
  def apply() = {
    Map(
      "v" -> "1",
      "tid" -> getProperty("analytics.accountTag","UA-43115970-1"),
      "cid" -> randomUUID().toString(),
      "t" -> "event",
      "dh" -> getProperty("analytics.host","localhost"),
      "ec" -> "channelshift",
      "ea" -> "card-referral"

    )
  }
}
object ChannelShiftController extends ChannelShift(ChannelShiftParams())

class ChannelShift(params:Map[String,String]) extends Controller{

  def redirect = Action{ request =>

    notifyGA(params)

    Redirect(getProperty("channel.shift.carers.redirection","error"))
  }

  private def notifyGA(params:Map[String,String]) = {

    val url = getUrl()
    val cox= url.openConnection().asInstanceOf[HttpURLConnection]
    cox.setDoOutput( true )
    cox.setDoInput ( true )
    cox.setInstanceFollowRedirects( false )
    cox.setRequestMethod( "POST" )

    val urlParameters  = params.map((t) => s"${t._1}=${t._2}").mkString("&")
    val postData       = urlParameters.getBytes( Charset.forName( "UTF-8" ))
    cox.setRequestProperty( "Content-Length", Integer.toString( postData.length ))
    cox.setUseCaches( false )

    writeData(cox,postData)
    //Need to request something from the response to actually initiate the request-response
    val response = cox.getInputStream.available()
    Logger.debug(s"Response available from GA:$response")


  }

  protected def getUrl() = URLWrapper("http://www.google-analytics.com/collect")

  protected def writeData(cox:HttpURLConnection, postData:Array[Byte]) = {
    Try(cox.getOutputStream().write(postData)) match {
      case Failure(e) => Logger.error("Error writing data to Google Analytics web service. ",e)
      case _ => Logger.debug("Written to Google Analytics URL Connection")
    }
  }

}

case class URLWrapper(s:String){
  val url = new URL(s)
  def openConnection() = url.openConnection()
}
