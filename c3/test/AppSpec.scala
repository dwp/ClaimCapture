import org.specs2.mutable.{Tags, Specification}
import app.StatutoryPaymentFrequency._
import models.PaymentFrequency

class AppSpec extends Specification with Tags {

  "package object app" should {
    "StatutoryPaymentFrequency should translate options into string" in {
      "for W" in {
        optionToString(Some(PaymentFrequency(Weekly))) shouldEqual "Weekly"
      }

      "for FN" in {
        optionToString(Some(PaymentFrequency(Fortnightly))) shouldEqual "Fortnightly"
      }

      "for 4W" in {
        optionToString(Some(PaymentFrequency(FourWeekly))) shouldEqual "Four-weekly"
      }

      "for M" in {
        optionToString(Some(PaymentFrequency(Monthly))) shouldEqual "Monthly"
      }

      "for other" in {
        optionToString(Some(PaymentFrequency(Other, Some("once per year")))) shouldEqual "Other: once per year"
      }

      "for None" in {
        optionToString(None) must beEmpty
      }
    }
  } section "unit"
}