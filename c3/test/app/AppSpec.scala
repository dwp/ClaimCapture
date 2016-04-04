package app

import org.specs2.mutable._
import app.StatutoryPaymentFrequency._

class AppSpec extends Specification {
  section("unit")
  "StatutoryPaymentFrequency" should {
    "convert frequencies to human readable ones" in  {
      mapToHumanReadableStringWithOther(Some(models.PaymentFrequency("Other", Some("The other")))) mustEqual "Other: The other"
      mapToHumanReadableStringWithOther(Some(models.PaymentFrequency("Other", None))) mustEqual "Other"
      mapToHumanReadableString(Some(models.PaymentFrequency("Four-Weekly"))) mustEqual "Four-weekly"
    }
  }
  section("unit")
}
