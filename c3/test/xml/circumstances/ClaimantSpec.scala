package xml.circumstances

import javax.xml.bind.DatatypeConverter

import gov.dwp.carers.security.encryption.EncryptorAES
import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain.{CircumstancesReportChange, Claim}
import org.specs2.mutable.{Specification, Tags}

class ClaimantSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO123456D"))
  val contact = "by post"
  val yourDetails = CircumstancesReportChange(
    fullName = "Mr Phil Joe Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  "Claimant" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Claim().update(yourDetails)
      val xml = Claimant.xml(claim)

      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary((xml \\ "ClaimantDetails" \\ "FullName" \\ "Answer").text)) shouldEqual yourDetails.fullName
      (xml \\ "ClaimantDetails" \\ "DateOfBirth" \\ "Answer").text shouldEqual yourDetails.dateOfBirth.`dd-MM-yyyy`
      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary((xml \\ "ClaimantDetails" \\ "NationalInsuranceNumber" \\ "Answer").text)) shouldEqual nationalInsuranceNr.stringify
    }
  } section "unit"
}