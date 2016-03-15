package app

import org.specs2.mutable._
import play.Logger

class MessageFilesSpec extends Specification {
  section("integration")
  "Property files" should {
    val source = scala.io.Source.fromFile("conf/messagelisting.properties")
    val linesFromSource = source.getLines.toList
    source.close()
    val enFiles = linesFromSource.filter(line => line.contains(".en.")).map(fullEnFilename => fullEnFilename.substring(0, fullEnFilename.indexOf(".en.")))
    val cyFiles = linesFromSource.filter(line => line.contains(".cy.")).map(fullEnFilename => fullEnFilename.substring(0, fullEnFilename.indexOf(".cy.")))
    val niFiles = List("niDirect")
    val singleAposPattern = ".*[^']'[^'].*"

    val enKeys = enFiles.map(
      enFile => {
        val enS = scala.io.Source.fromFile("conf/en/%s.en.properties".format(enFile))
        val linesFromEnS = enS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
        val enKeys = linesFromEnS.map(pair => pair.split("=").map(k => k.trim))
        enS.close()
        enKeys
      }
    ).flatMap(y => y).map(a => a(0))

    val cyKeys = cyFiles.map(
      cyFile => {
        val cyS = scala.io.Source.fromFile("conf/cy/%s.cy.properties".format(cyFile))
        val linesFromCyS = cyS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
        if (linesFromCyS.toString.contains("translate")) Logger.warn(s"$cyFile contains English text waiting for translation to Welsh")
        val cyKeys = linesFromCyS.map(pair => pair.split("=").map(k => k.trim))
        cyS.close()
        cyKeys
      }
    ).flatMap(y => y).map(a => a(0))

    val enValues = {
      enFiles.map(
        enFile => {
          val enS = scala.io.Source.fromFile("conf/en/%s.en.properties".format(enFile))
          val linesFromEnS = enS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
          val values = linesFromEnS.map(pair => pair.split("=").map(k => k.trim))
          enS.close()
          values
        }
      ).flatMap(y => y).map(a => if(a.length>1)a(1))
    }

    val cyValues = {
      enFiles.map(
        enFile => {
          val enS = scala.io.Source.fromFile("conf/cy/%s.cy.properties".format(enFile))
          val linesFromEnS = enS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
          val values = linesFromEnS.map(pair => pair.split("=").map(k => k.trim))
          enS.close()
          values
        }
      ).flatMap(y => y).map(a => if(a.length>1)a(1))
    }

    val niValues = {
      niFiles.map(
        niFile => {
          val enS = scala.io.Source.fromFile("conf/en/%s.en.properties".format(niFile))
          val linesFromEnS = enS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
          val values = linesFromEnS.map(pair => pair.split("=").map(k => k.trim))
          enS.close()
          values
        }
      ).flatMap(y => y).map(a => if(a.length>1)a(1))
    }

    "have corresponding Welsh file for each English file" in {
      enFiles.filterNot(enFile => cyFiles.contains(enFile)) must beEmpty
    }

    "have corresponding English file for each Welsh file" in {
      cyFiles.filterNot(cyFile => enFiles.contains(cyFile)) must beEmpty
    }

    "each key in a English property file must have a corresponding key in the Welsh file" in {
      enKeys.filterNot(enKey => cyKeys.contains(enKey)) must beEmpty
    }

    "each key in a Welsh property file must have a corresponding key in the English file" in {
      cyKeys.filterNot(cyKey => enKeys.contains(cyKey)) must beEmpty
    }

    "not contain any single apostrophes in english files" in {
      val badMessages=enValues.filter(value=>value.toString.matches(singleAposPattern))
      badMessages.foreach( message => println("ERROR - Bad en message single apostrophe in message:"+message))
      badMessages must beEmpty
    }

    "not contain any single apostrophes in welsh files" in {
      val badMessages=cyValues.filter(value=>value.toString.matches(singleAposPattern))
      badMessages.foreach( message => println("ERROR - Bad cy message single apostrophe in message:"+message))
      badMessages must beEmpty
    }

    "not contain any single apostrophes in ni files" in {
      niValues.size >0 must beTrue
      val badMessages=niValues.filter(value=>value.toString.matches(singleAposPattern))
      badMessages.foreach( message => println("ERROR - Bad ni message single apostrophe in message:"+message))
      badMessages must beEmpty
    }
  }
  section("integration")
}
