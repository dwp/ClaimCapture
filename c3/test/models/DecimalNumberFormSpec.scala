package models

import org.specs2.mutable.Specification
import play.api.data.{Forms, FormError, Form}
import play.api.data.Forms._
import controllers.mappings.Mappings._

class DecimalNumberFormSpec extends Specification {
  "Decimal Number" should {
    "accept single decimal input" in {
      val value = "500.5"

      createDecimalNumberForm(value).get should beLike { case Some(d) => d shouldEqual value }
    }

    "accept number with no decimal" in {
      val value = "500"

      createDecimalNumberForm(value).get should beLike { case Some(d) => d shouldEqual value }
    }

    "reject characters" in {
      val value = "abc"

      val f: Form[Option[String]] = createDecimalNumberForm(value)
      f.error("decimalNumber") should beLike { case Some(fe: FormError) => fe.message shouldEqual "decimal.invalid" }
    }

    "reject number with dot and no decimal" in {
      val value = "500."

      val f: Form[Option[String]] = createDecimalNumberForm(value)
      f.error("decimalNumber") should beLike { case Some(fe: FormError) => fe.message shouldEqual "decimal.invalid" }
    }

    "reject 3 decimal input" in {
      val value = "500.501"

      val f: Form[Option[String]] = createDecimalNumberForm(value)
      f.error("decimalNumber") should beLike { case Some(fe: FormError) => fe.message shouldEqual "decimal.invalid" }
    }
  }

  private def createDecimalNumberForm(decimalNumber: String)
    = Form("decimalNumber" -> optional(Forms.text verifying validDecimalNumber)).bind(Map("decimalNumber" -> decimalNumber))
}