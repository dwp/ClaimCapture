package xml

import org.specs2.mutable.{Tags, Specification}
import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import models.domain.Circs


class AdditionalInfoSpec extends Specification with Tags {

  val otherInfo = "Some other info"

  "Additional Info" should {
    "generate xml" in {
      val claim = Circs()().update(CircumstancesOtherInfo(otherInfo))
      val xml = AdditionalInfo.xml(claim.asInstanceOf[Circs])

      (xml \\ "OtherChanges").text shouldEqual otherInfo
    }

  } section "unit"
}
