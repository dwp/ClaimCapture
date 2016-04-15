package xml.circumstances

import javax.xml.bind.DatatypeConverter

import gov.dwp.carers.security.encryption.EncryptorAES
import models.SortCode
import models.domain.{Claim, _}
import models.view.CachedChangeOfCircs
import models.yesNo.YesNoWith2Text
import org.specs2.mutable._
import utils.WithApplication

class PaymentSpec extends Specification {
  def decrypt(value: String)={
    (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(value))
  }

  section("unit")
  "Circs payment change" should {
    "generate correct xml including stripped account number spaces" in new WithApplication {
      val paymentChange=CircumstancesPaymentChange(YesNoWith2Text("No", None, Some("By Cheque")),
                "John Smith",
                "Barclays Preston",
                SortCode("20", "56", "57" ),
                " 1234 1234",
                "",
                "Weekly",
                Some("Testing circs payment account with spaces")
                )
      val claim = Claim(CachedChangeOfCircs.key).update(paymentChange)
      val xml = PaymentChange.xml(claim)

      (xml \\ "PaymentChange" \\ "PaidIntoAccountDetails" \\ "PaidIntoAccount" \\ "Answer").text shouldEqual "No"
      (xml \\ "PaymentChange" \\ "PaidIntoAccountDetails" \\ "MethodOfPayment" \\ "Answer").text shouldEqual "By Cheque"
      decrypt((xml \\ "PaymentChange" \\ "AccountDetails" \\ "HolderName" \\ "Answer").text) shouldEqual  "John Smith"
      decrypt((xml \\ "PaymentChange" \\ "AccountDetails" \\ "BuildingSocietyDetails" \\ "SortCode" \\ "Answer").text) shouldEqual  "205657"
      decrypt((xml \\ "PaymentChange" \\ "AccountDetails" \\ "BuildingSocietyDetails" \\ "AccountNumber" \\ "Answer").text) shouldEqual  "12341234"
      (xml \\ "PaymentChange" \\ "OtherChanges" \\ "Answer").text shouldEqual "Testing circs payment account with spaces"
    }
  }
  section("unit")
}
