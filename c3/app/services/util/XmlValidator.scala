package services.util

import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import org.xml.sax.SAXException
import play.api.Play
import java.io.{ByteArrayInputStream, InputStreamReader}
import play.Logger
import play.api.Play.current

/**
 *  javax.xml.validation.Schema is not thread safe
 *  Don't convert to object
 */
case class XmlValidator(xml: String, xsdFileName: String) {
  def validate : Boolean = {
    val xsdStream = Play.classloader.getResourceAsStream(xsdFileName)
    try {
      val schemaLang = "http://www.w3.org/2001/XMLSchema"
      val factory = SchemaFactory.newInstance(schemaLang)
      val schema = factory.newSchema(new StreamSource(xsdStream))
      val validator = schema.newValidator()
      validator.validate(new StreamSource(new InputStreamReader(new ByteArrayInputStream(xml.getBytes))))
    } catch {
      case ex: SAXException => Logger.error(ex.getMessage); return false
      case ex: Exception => Logger.error(ex.getMessage); return false
    }
    true
  }
}
