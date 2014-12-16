package models

import play.api.data.Form
import org.specs2.mutable.Specification
import controllers.mappings.Mappings._
import app.StatutoryPaymentFrequency._

class PaymentFrequencyFormSpec extends Specification {

  "Payment Frequency Validation" should {

    "accept valid input (with other filled in)" in {
      Form("howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly)).bind(Map("howOften.frequency" -> Weekly)).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        f => f must equalTo(PaymentFrequency(Weekly, None))
      )
    }
        
    "accept valid input (with other filled in)" in {
      val howOften_frequency_other = "Every day and twice on Sundays"
      Form("howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly)).bind(Map("howOften.frequency" -> Other, "howOften.frequency.other" -> howOften_frequency_other)).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        f => f must equalTo(PaymentFrequency(Other, Some(howOften_frequency_other)))
      )
    }

    "reject invalid input (with other not filled in)" in {
      Form("howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly)).bind(Map("howOften.frequency" -> Other)).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.paymentFrequency"),
        dateMonthYear => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
}