package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, PropertyAndRent}
import controllers.Mappings.yes

class PropertyAndRentedOutSpec extends Specification with Tags {

  "PropertyRentedOut" should {
    "generate xml" in {
      val claim = Claim().update(PropertyAndRent(yes, yes))
      val xml = PropertyRentedOut.xml(claim)

      (xml \\ "RentOutProperty").text shouldEqual yes
      (xml \\ "SubletHome").text shouldEqual yes
    }
  }
}
