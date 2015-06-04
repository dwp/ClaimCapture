package app

import org.specs2.mutable.{Tags, Specification}
import play.Logger
import play.api.i18n.Messages

class MessageFilesSpec extends Specification with Tags {
  "Property files" should {

    val source = scala.io.Source.fromFile("conf/messagelisting.properties")
    val linesFromSource = source.getLines.toList
    source.close()
    val enFiles = linesFromSource.filter(line => line.contains(".en.")).map(fullEnFilename => fullEnFilename.substring(0, fullEnFilename.indexOf(".en.")))
    val cyFiles = linesFromSource.filter(line => line.contains(".cy.")).map(fullEnFilename => fullEnFilename.substring(0, fullEnFilename.indexOf(".cy.")))

    val enKeys = enFiles.map(
      enFile => {
        println("conf/en/%s.en.properties".format(enFile))
        val enS = scala.io.Source.fromFile("conf/en/%s.en.properties".format(enFile))
        val linesFromEnS = enS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
        val enKeys = linesFromEnS.map(pair => pair.split("=").map(k => k.trim))
        enS.close()
        enKeys
      }
    ).flatMap(y => y).map(a => a(0))

    val cyKeys = cyFiles.map(
      cyFile => {
        println("conf/cy/%s.cy.properties".format(cyFile))
        val cyS = scala.io.Source.fromFile("conf/cy/%s.cy.properties".format(cyFile))
        val linesFromCyS = cyS.getLines.filterNot(_.isEmpty).filterNot(s => s.startsWith("#")).toList
        if (linesFromCyS.toString.contains("translate")) Logger.warn(s"$cyFile contains English text waiting for translation to Welsh")
        val cyKeys = linesFromCyS.map(pair => pair.split("=").map(k => k.trim))
        cyS.close()
        cyKeys
      }
    ).flatMap(y => y).map(a => a(0))

    "have corresponding Welsh file for each English file" in {
      enFiles.filterNot(enFile => cyFiles.contains(enFile)) must beEmpty
    }

    "have corresponding English file for each Welsh file" in {
      cyFiles.filterNot(cyFile => enFiles.contains(cyFile)) must beEmpty
    }

    "each key in a English property file must have a corresponding key in the Welsh file" in {
      println(enKeys.filterNot(enKey => cyKeys.contains(enKey)).toString)
      enKeys.filterNot(enKey => cyKeys.contains(enKey)) must beEmpty
    }

    "each key in a Welsh property file must have a corresponding key in the English file" in {
      cyKeys.filterNot(cyKey => enKeys.contains(cyKey)) must beEmpty
    }
  }  section "integration"
}
