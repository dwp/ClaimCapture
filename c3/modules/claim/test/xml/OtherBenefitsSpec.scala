package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{Street, Town, NationalInsuranceNumber, MultiLineAddress}
import controllers.Mappings.yes
import controllers.Mappings.no
import app.XMLValues

class OtherBenefitsSpec extends Specification with Tags {

  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))

  val nationalInsuranceNrOption = Some(nationalInsuranceNr)
  val address = Some(MultiLineAddress(Street(Some("line1")), Some(Town(Some("line2"), Some("line3")))))
  val postcode = Some("SE1 6EH")

  val fullName = "fullName"
  val benefitName = "benefitName"
  val employersName = Some("employersName")
  val empty = ""

  val howMuch = Some("")

  val statutorySickPay = StatutorySickPay(haveYouHadAnyStatutorySickPay = yes, employersName = employersName, employersAddress = address, employersPostcode = postcode)
  val otherStatutoryPay = OtherStatutoryPay(otherPay = yes, employersName = employersName, employersAddress = address, employersPostcode = postcode)

  "OtherBenefits" should {

    "generate xml when data is present" in {
      val otherMoneySection = Section(OtherMoney, statutorySickPay :: otherStatutoryPay :: Nil)

      val claim = Claim().update(otherMoneySection).asInstanceOf[Claim]
      val otherBenefitsXml = OtherBenefits.xml(claim)

      val extraMoneyXml = otherBenefitsXml \\ "ExtraMoney"
      extraMoneyXml.text shouldEqual XMLValues.NotAsked

      (otherBenefitsXml \\ "OtherMoneySSP").text mustEqual yes
      val otherMoneySSPDetailsXml = otherBenefitsXml \\ "OtherMoneySSPDetails"

      (otherMoneySSPDetailsXml \\ "Name").text mustEqual employersName.get
      (otherMoneySSPDetailsXml \\ "Address" \\ "Line").theSeq(0).text mustEqual address.get.lineOne.get
      (otherMoneySSPDetailsXml \\ "Address" \\ "Line").theSeq(1).text mustEqual address.get.lineTwo.get
      (otherMoneySSPDetailsXml \\ "Address" \\ "Line").theSeq(2).text mustEqual address.get.lineThree.get
      (otherMoneySSPDetailsXml \\ "Address" \\ "PostCode").text mustEqual postcode.get

      (otherBenefitsXml \\ "OtherMoneySMP").text mustEqual yes
      val otherMoneySMPDetailsXml = otherBenefitsXml \\ "OtherMoneySMPDetails"
      (otherMoneySMPDetailsXml \\ "Name").text mustEqual employersName.get
      (otherMoneySMPDetailsXml \\ "Address" \\ "Line").theSeq(0).text mustEqual address.get.lineOne.get
      (otherMoneySMPDetailsXml \\ "Address" \\ "Line").theSeq(1).text mustEqual address.get.lineTwo.get
      (otherMoneySMPDetailsXml \\ "Address" \\ "Line").theSeq(2).text mustEqual address.get.lineThree.get
      (otherMoneySMPDetailsXml \\ "Address" \\ "PostCode").text mustEqual postcode.get
    }

    "generate xml when data is missing" in {
      val claim = Claim().update(Section(OtherMoney, Nil)).asInstanceOf[Claim]
      val otherMoneyXml = OtherBenefits.xml(claim)

      val extraMoneyXml = otherMoneyXml \\ "ExtraMoney"
      extraMoneyXml.text shouldEqual XMLValues.NotAsked
      (otherMoneyXml \\ "ExtraMoneyDetails").text must beEmpty

      (otherMoneyXml \\ "OtherMoneySSP").text mustEqual no
      (otherMoneyXml \\ "OtherMoneySSPDetails").text must beEmpty

      (otherMoneyXml \\ "OtherMoneySMP").text mustEqual no
      (otherMoneyXml \\ "OtherMoneySMPDetails").text must beEmpty
    }
  } section "unit"
}