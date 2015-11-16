package controllers

import utils.WithApplication
import java.io.InputStream
import java.net.{URL, HttpURLConnection}
import java.util.UUID._

import app.ConfigProperties._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.Logger
import play.api.test.FakeRequest
import play.api.test.Helpers._
import collection.JavaConverters._

/**
 * Created by valtechuk on 25/03/2015.
 */
class ChannelShiftSpec extends Specification with Mockito{

  "Channel Shift Controller" should {

    "preserve parameter data" in new WithApplication {

      val params = Map("v" -> "1",
      "tid" -> "UA-43115970-1",
      "cid" -> randomUUID().toString(),
      "t" -> "event",
      "dh" -> "localhost",
      "ec" -> "channelshift",
      "ea" -> "card-referral")

      val url = mock[URLWrapper]
      url.openConnection() returns mock[HttpURLConnection]
      url.openConnection().getInputStream returns mock[InputStream]
      url.openConnection().getInputStream().available() returns -1

      val cs = new ChannelShiftTestable(params, url)

      val result = cs.redirect(FakeRequest())

      status(result) mustEqual SEE_OTHER

      ChannelShiftTestable.synchronized{
        ChannelShiftTestable.params mustEqual params
      }

    }
  }
  section ("unit", "slow")
}

class ChannelShiftTestable(params:Map[String,String],url:URLWrapper) extends ChannelShift(params) {
  override protected def writeData(cox: HttpURLConnection, postData: Array[Byte]): Unit = {
    Logger.info(s"postData Array: $postData")
    val s = new String(postData,"UTF-8")

    Logger.info(s"postData as string:$s")
    val a = s.split("&")
    val map = a.map(_.split("=")).map(array => array(0)->array(1)).toMap
    ChannelShiftTestable.synchronized{
      ChannelShiftTestable.params = map
    }
  }

  override protected def getUrl(): URLWrapper = {
    url
  }
}

object ChannelShiftTestable{
  var params = Map.empty[String,String]
}
