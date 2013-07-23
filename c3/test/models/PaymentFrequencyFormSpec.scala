package models

import play.api.data.Form
import org.specs2.mutable.Specification
import controllers.Mappings._

class PaymentFrequencyFormSpec extends Specification {

  "Payment Frequency Validation" should {

    "accept valid input (with other filled in)" in {
      Form("howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly)).bind(Map("howOften.frequency" -> "Weekly")).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        f => f must equalTo(PaymentFrequency("Weekly", None))
      )
    }
        
    "accept valid input (with other filled in)" in {
      Form("howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly)).bind(Map("howOften.frequency" -> "Other", "howOften.other" -> "Every day")).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        f => f must equalTo(PaymentFrequency("Other", Some("Every day")))
      )
    }

    "reject invalid input (with other not filled in)" in {
      Form("howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly)).bind(Map("howOften.frequency" -> "Other")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.paymentFrequency"),
        dateMonthYear => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
}