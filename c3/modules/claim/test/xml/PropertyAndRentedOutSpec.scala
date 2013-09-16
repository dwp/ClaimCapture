package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.Claim
import app.XMLValues._

class PropertyAndRentedOutSpec extends Specification with Tags {

  "PropertyRentedOut" should {
    "generate xml" in {
      val claim = Claim()
      val xml = PropertyRentedOut.xml(claim)

      (xml \\ "RentOutProperty").text shouldEqual NotAsked
      (xml \\ "SubletHome").text shouldEqual NotAsked
    }
  }
}
