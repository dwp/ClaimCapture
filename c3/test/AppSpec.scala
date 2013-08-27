import org.specs2.mutable.{Tags, Specification}
import app.StatutoryPaymentFrequency
import models.PaymentFrequency

class AppSpec extends Specification with Tags {

  "package object app" should {
    "StatutoryPaymentFrequency should translate options into string" in {
      "for W" in {
        StatutoryPaymentFrequency.optionToString(Some(PaymentFrequency("W"))) shouldEqual "Weekly"
      }

      "for FN" in {
        StatutoryPaymentFrequency.optionToString(Some(PaymentFrequency("FN"))) shouldEqual "Fortnightly"
      }

      "for 4W" in {
        StatutoryPaymentFrequency.optionToString(Some(PaymentFrequency("4W"))) shouldEqual "Four-weekly"
      }

      "for M" in {
        StatutoryPaymentFrequency.optionToString(Some(PaymentFrequency("M"))) shouldEqual "Monthly"
      }

      "for other" in {
        StatutoryPaymentFrequency.optionToString(Some(PaymentFrequency("other", Some("once per year")))) shouldEqual "Other: once per year"
      }

      "for None" in {
        StatutoryPaymentFrequency.optionToString(None) must beEmpty
      }
    }
  } section "unit"
}