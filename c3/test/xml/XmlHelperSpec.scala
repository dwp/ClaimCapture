package xml

import javax.xml.bind.DatatypeConverter
import gov.dwp.carers.security.encryption.EncryptorAES
import org.specs2.mutable._
import utils.WithApplication

class XmlHelperSpec extends Specification {

  val employedQuestion = "Your employment history"

  section("unit")
  "XMLHelper" should {
    "construct a basic question" in {
      "when question has an answer of type text." in new WithApplication {
        XMLHelper.question(<Test/>,"s7.g2", "Yes").toString shouldEqual "<Test><QuestionLabel>"+employedQuestion+"</QuestionLabel><Answer>Yes</Answer></Test>"
      }

      "when question has an answer of type boolean." in new WithApplication {
        XMLHelper.question(<Test/>,"s7.g2", true).toString shouldEqual "<Test><QuestionLabel>"+employedQuestion+"</QuestionLabel><Answer>Yes</Answer></Test>"
      }

      "when question has an answer of type nodeSeq." in new WithApplication {
        XMLHelper.question(<Test/>,"s7.g2", <myNode>hello</myNode>).toString shouldEqual "<Test><QuestionLabel>"+employedQuestion+"</QuestionLabel><Answer><myNode>hello</myNode></Answer></Test>"
      }

      "when question with question label having two arguments." in new WithApplication {
        XMLHelper.question(<Test/>,"dynamicDate.helper", <myNode>hello</myNode>,"arg1","arg2").toString shouldEqual "<Test><QuestionLabel>For example, 27 arg1 arg2</QuestionLabel><Answer><myNode>hello</myNode></Answer></Test>"
      }
    }
  }

  "construct an other question" in {
    "when question has other option" in new WithApplication{
      XMLHelper.questionOther(<Test/>,"s7.g2", "Other", Some("Maybe")).toString shouldEqual "<Test><QuestionLabel>"+employedQuestion+"</QuestionLabel><Other>Maybe</Other><Answer>Other</Answer></Test>"
    }

    "when question has why option" in new WithApplication{
      XMLHelper.questionWhy(<Test/>,"s7.g2", "No", Some("Maybe"),"obtainInfoWhy" ).toString shouldEqual "<Test><QuestionLabel>"+employedQuestion+"</QuestionLabel><Answer>No</Answer><Why><QuestionLabel>List anyone you don't want to be contacted and say why.</QuestionLabel><Answer>Maybe</Answer></Why></Test>"
    }

    "when question is about currency" in new WithApplication{
      XMLHelper.questionCurrency(<Test/>,"s7.g2", Some("122.12")).toString shouldEqual "<Test><QuestionLabel>"+employedQuestion+"</QuestionLabel><Answer><Currency>GBP</Currency><Amount>122.12</Amount></Answer></Test>"
    }
  }

  "can encrypt data with random salt" in {
    "when receives a string" in new WithApplication {
      val text = "text"
      val encrypted1 = XMLHelper.encrypt(text);
      val encrypted2 = XMLHelper.encrypt(text);
      encrypted1 shouldNotEqual text
      encrypted2 shouldNotEqual encrypted1
      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(encrypted1)) shouldEqual text
    }
  }
  section("unit")
}
