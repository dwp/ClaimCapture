package xml.claim

import javax.xml.bind.DatatypeConverter

import app.PaymentTypes
import gov.dwp.carers.security.encryption.EncryptorAES
import models.{SortCode, DayMonthYear}
import models.domain.{Claim, _}
import models.view.{CachedClaim}
import org.specs2.mutable._
import utils.WithApplication

class PaymentSpec extends Specification {
  def decrypt(value: String) = {
    (new EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary(value))
  }

  section("unit")
  "Claim xml generation for payment" should {
    "Generate correct xml including account number stripped spaces " in new WithApplication {
      var claim = new Claim(CachedClaim.key, uuid = "1234")
      val claimDate = ClaimDate(DayMonthYear(20, 3, 2016))
      val bankDetails = new BankBuildingSocietyDetails("John Smith",
        "Barclays Preston",
        SortCode("20", "56", "57"),
        " 1234 1234",
        "")
      val paymentInfo = new HowWePayYou("yes", Some(bankDetails), "Weekly")
      val xml = Payment.xml(claim + claimDate + paymentInfo)

      (xml \\ "Payment" \\ "InitialAccountQuestion" \\ "Answer").text shouldEqual "Yes"
      decrypt((xml \\ "Payment" \\ "HolderName" \\ "Answer").text) shouldEqual "John Smith"
      decrypt((xml \\ "Payment" \\ "BuildingSocietyDetails" \\ "SortCode" \\ "Answer").text) shouldEqual "205657"
      decrypt((xml \\ "Payment" \\ "BuildingSocietyDetails" \\ "AccountNumber" \\ "Answer").text) shouldEqual "12341234"
      (xml \\ "Payment" \\ "BuildingSocietyDetails" \\ "Name" \\ "Answer").text shouldEqual "Barclays Preston"
    }
  }
  section("unit")
}
