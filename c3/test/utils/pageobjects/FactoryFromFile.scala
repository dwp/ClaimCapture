package utils.pageobjects

import scala.io.Source
import scala.util.matching.Regex

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
object FactoryFromFile {

  // used to parse claim file's lines. Expect 3 information columns, followed by attribute and then value of attribute.
  private val Extractor = """^ *([^,"]+|"[^"]+") *, *([^,"]+|"[^"]+") *, *([^,"]+|"[^"]+") *, *([^,"]+|"[^"]+") *,? *([^,"]+|"[^"]+")?.*""".r

  def buildFromFile(fileName: String, extractor: (String) => (String) => Unit) = {
    Source fromURL (getClass getResource fileName) getLines() foreach {
      line =>
        try {
          val Extractor(column1, column2, column3, column_name, column_value) = line
          if (null != column_name && null != column_value)
            extractor(column_name.replaceAll("\"", "").trim)(column_value.replaceAll("\"", "").trim)
        }
        catch {
          case e: Exception => throw new PageObjectException(message = "Error while parsing file " + fileName + ". ", exception = e)
        }
    }
  }
}
