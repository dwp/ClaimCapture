package xml

import org.specs2.mutable.{Tags, Specification}
import app.XMLValues._

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
  }
}