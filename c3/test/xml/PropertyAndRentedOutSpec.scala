package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim}
import controllers.Mappings.yes

class PropertyAndRentedOutSpec extends Specification with Tags {

  "PropertyRentedOut" should {
    "generate xml" in {
      val claim = Claim()
      val xml = PropertyRentedOut.xml(claim)

      (xml \\ "RentOutProperty").text shouldEqual "Not asked"
      (xml \\ "SubletHome").text shouldEqual "Not asked"
    }
  }
}
