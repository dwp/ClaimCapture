import org.specs2.mutable.{Tags, Specification}
import app.StatutoryPaymentFrequency._
import models.PaymentFrequency

class AppSpec extends Specification with Tags {

  "package object app" should {
    "StatutoryPaymentFrequency should translate options into string" in {
      "for W" in {
        mapToHumanReadableString(Some(PaymentFrequency(Weekly))) shouldEqual "Weekly"
      }

      "for FN" in {
        mapToHumanReadableString(Some(PaymentFrequency(Fortnightly))) shouldEqual "Fortnightly"
      }

      "for 4W" in {
        mapToHumanReadableString(Some(PaymentFrequency(FourWeekly))) shouldEqual "Four-Weekly"
      }

      "for M" in {
        mapToHumanReadableString(Some(PaymentFrequency(Monthly))) shouldEqual "Monthly"
        mapToHumanReadableStringWithOther(Some(PaymentFrequency(Monthly))) shouldEqual "Monthly"
      }

      "for Other" in {
        mapToHumanReadableStringWithOther(Some(PaymentFrequency(Other, Some("once per year")))) shouldEqual "Other"
      }

      "for None" in {
        mapToHumanReadableString(None) must beEmpty
      }
    }
  } section "unit"
}