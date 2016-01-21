package xml

import models.view.CachedClaim
import org.specs2.mutable._
import models.domain.Claim
import models.NationalInsuranceNumber
import scala.xml.Elem
import java.util.Date
import java.text.SimpleDateFormat

class DWPBodySpec extends Specification {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO123456D"))

  section("unit")
  "DWPBody" should {
    "Should generate a full XML with signature" in {
      val xml = new DWPBodyWithShortXMLClaim xml(new Claim(CachedClaim.key),"NB1212X")
      val date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date())
      (xml \\ "DWPBody" \ "DWPCATransaction" \ "DateTimeGenerated").text mustEqual date
      (xml \\ "DWPBody" \ "DWPCATransaction" \ "@id").text mustEqual "NB1212X"
      (xml \\ "DWPBody" \ "DWPCATransaction" \ "TransactionId").text mustEqual "NB1212X"
      (xml \\ "Signature" \\ "DigestValue").text.isEmpty must beFalse
      (xml \\"Signature" \\ "SignatureValue").text.isEmpty must beFalse
    }
  }
  section("unit")
}

private class DWPBodyWithShortXMLClaim extends DWPBody {
  override protected def coreXml(claim: Claim): Elem = <DWPCAClaim>
    <DateOfClaim>
      <QuestionLabel>When do you want your Carer's Allowance claim to start?</QuestionLabel>
      <Answer>01-01-2010</Answer>
    </DateOfClaim>
    <Claimant>
      <Surname>CaseThree</Surname>
      <OtherNames>Test Middle</OtherNames>
      <OtherSurnames>Smithson</OtherSurnames>
      <Title>Mr</Title>
      <DateOfBirth>01-01-1931</DateOfBirth>
      <NationalInsuranceNumber>JB486278C</NationalInsuranceNumber>
      <Address>
        <Line>3 Preston Road</Line>
        <Line>Preston</Line>
        <Line>Lancashire</Line>
        <PostCode>PR1 2TH</PostCode>
      </Address>
      <DayTimePhoneNumber>01772 888901</DayTimePhoneNumber>
      <MobileNumber>0771 5419808</MobileNumber>
      <MaritalStatus>Single</MaritalStatus>
      <TextPhoneContact>
        <QuestionLabel>text.phone</QuestionLabel>
        <Answer>No</Answer>
      </TextPhoneContact>
    </Claimant></DWPCAClaim>
}
