package xml.circumstances

import javax.xml.bind.DatatypeConverter

import gov.dwp.carers.security.encryption.EncryptorAES
import models.view.CachedChangeOfCircs
import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain.{CircumstancesReportChange, Claim}
import org.specs2.mutable._
import utils.WithApplication

class ClaimantSpec extends Specification {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO123456D"))
  val contact = "by post"


  def getCircumstancesReportChange = {
    CircumstancesReportChange(
      fullName = "Mr Phil Joe Smith",
      nationalInsuranceNumber = nationalInsuranceNr,
      dateOfBirth = DayMonthYear(1, 1, 1963),
      wantsContactEmail = Some("Yes"),
      email = Some("joe@smith.co.uk"),
      emailConfirmation = Some("joe@smith.co.uk")
    )
  }
  "Claimant" should {
    "generate Claimant xml from a given circumstances" in new WithApplication() {
      val yourDetails = getCircumstancesReportChange
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails)
      val xml = Claimant.xml(claim)

      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary((xml \\ "ClaimantDetails" \\ "FullName" \\ "Answer").text)) shouldEqual yourDetails.fullName
      (xml \\ "ClaimantDetails" \\ "DateOfBirth" \\ "Answer").text shouldEqual yourDetails.dateOfBirth.`dd-MM-yyyy`
      (xml \\ "ClaimantDetails" \\ "WantsContactEmail" \\ "Answer").text shouldEqual "Yes"
      (xml \\ "ClaimantDetails" \\ "Email" \\ "Answer").text shouldEqual "joe@smith.co.uk"
      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary((xml \\ "ClaimantDetails" \\ "NationalInsuranceNumber" \\ "Answer").text)) shouldEqual nationalInsuranceNr.stringify
    }
  }
  section("unit")
}
