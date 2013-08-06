package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.NationalInsuranceNumber
import scala.Some
import models.MultiLineAddress
import controllers.Mappings.yes
import controllers.Mappings.no

class OtherBenefitsSpec extends Specification with Tags {

  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))

  val nationalInsuranceNrOption = Some(nationalInsuranceNr)
  val address = Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3")))
  val postcode = Some("SE1 6EH")

  val fullName = "fullName"
  val benefitName = "benefitName"
  val employersName = Some("employersName")
  val empty = ""

  val howMuch = Some("")

  val moneyPaidToSomeoneElse = MoneyPaidToSomeoneElseForYou(yes, NoRouting)
  val personWhoGetsThisMoney = PersonWhoGetsThisMoney(fullName, nationalInsuranceNrOption, benefitName, NoRouting)
  val contactDetails = PersonContactDetails(address, postcode, NoRouting)
  val statutorySickPay = StatutorySickPay(haveYouHadAnyStatutorySickPay = yes, employersName = employersName, employersAddress = address, employersPostcode = postcode)
  val otherStatutoryPay = OtherStatutoryPay(otherPay = yes, employersName = employersName, employersAddress = address, employersPostcode = postcode)

  "OtherBenefits" should {

    "generate xml when data is present" in {
      val otherMoneySection = Section(OtherMoney, moneyPaidToSomeoneElse :: personWhoGetsThisMoney :: contactDetails :: statutorySickPay :: otherStatutoryPay :: Nil)

      val claim = Claim().update(otherMoneySection)
      val otherBenefitsXml = OtherBenefits.xml(claim)

      val extraMoneyXml = otherBenefitsXml \\ "ExtraMoney"
      extraMoneyXml.text shouldEqual yes

      val extraMoneyDetailsXml = otherBenefitsXml \\ "ExtraMoneyDetails"
      (extraMoneyDetailsXml \\ "BenefitName").text shouldEqual benefitName
      (extraMoneyDetailsXml \\ "RecipientName").text shouldEqual fullName
      (extraMoneyDetailsXml \\ "RecipientAddress" \\ "Line").theSeq(0).text mustEqual address.get.lineOne.get
      (extraMoneyDetailsXml \\ "RecipientAddress" \\ "Line").theSeq(1).text mustEqual address.get.lineTwo.get
      (extraMoneyDetailsXml \\ "RecipientAddress" \\ "Line").theSeq(2).text mustEqual address.get.lineThree.get
      (extraMoneyDetailsXml \\ "RecipientAddress" \\ "PostCode").text mustEqual postcode.get
      (extraMoneyDetailsXml \\ "ReferenceNumber").text mustEqual nationalInsuranceNr.toXmlString

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
      val claim = Claim().update(Section(OtherMoney, Nil))
      val otherMoneyXml = OtherBenefits.xml(claim)

      val extraMoneyXml = otherMoneyXml \\ "ExtraMoney"
      extraMoneyXml.text shouldEqual no
      (otherMoneyXml \\ "ExtraMoneyDetails").text mustEqual ""

      (otherMoneyXml \\ "OtherMoneySSP").text mustEqual no
      (otherMoneyXml \\ "OtherMoneySSPDetails").text mustEqual ""

      (otherMoneyXml \\ "OtherMoneySMP").text mustEqual no
      (otherMoneyXml \\ "OtherMoneySMPDetails").text mustEqual ""
    }
  } section "unit"

}