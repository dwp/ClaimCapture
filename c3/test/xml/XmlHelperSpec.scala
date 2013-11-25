package xml

import org.specs2.mutable.{Tags, Specification}
import app.XMLValues._
import play.api.test.WithApplication

class XMLHelperSpec extends Specification with Tags {

  "XMLHelper" should {
    "title case yes/no answers" in {
      "when yes" in {
        XMLHelper.titleCase("yes") shouldEqual "Yes"
      }

      "when no" in {
        XMLHelper.titleCase("no") shouldEqual "No"
      }

      "when y" in {
        XMLHelper.titleCase("y") shouldEqual "Y"
      }

      "when empty" in {
        XMLHelper.titleCase("") shouldEqual ""
      }

      "when null" in {
        XMLHelper.titleCase(null) shouldEqual ""
      }
    }

    "convert boolean string to yes/no" in {
      "when true" in {
        XMLHelper.booleanStringToYesNo("true") shouldEqual Yes
      }

      "when false" in {
        XMLHelper.booleanStringToYesNo("false") shouldEqual No
      }

      "when other" in {
        XMLHelper.booleanStringToYesNo("other") shouldEqual "other"
      }

      "when empty" in {
        XMLHelper.booleanStringToYesNo("") shouldEqual ""
      }

      "when null" in {
        XMLHelper.booleanStringToYesNo(null) shouldEqual ""
      }
    }

    "construct a basic question" in {
      "when question is employed and answer yes" in new WithApplication {
        XMLHelper.question(<Test/>,"s7.g1", "Yes").toString shouldEqual "<Test><QuestionLabel>Have you been employed?</QuestionLabel><Answer>Yes</Answer></Test>"
      }
    }
  }

  "construct an other question" in {
    "when question is employed and answer yes" in new WithApplication{
      XMLHelper.questionOther(<Test/>,"s7.g1", "Other", Some("Maybe")).toString shouldEqual "<Test><QuestionLabel>Have you been employed?</QuestionLabel><Other>Maybe</Other><Answer>Other</Answer></Test>"
    }
  }
}