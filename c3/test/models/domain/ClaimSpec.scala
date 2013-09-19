package models.domain

import org.specs2.mutable.Specification
import models.{DayMonthYear, LivingInUK}
import models.yesNo.YesNoWithDate

class ClaimSpec extends Specification {
  val claim = Claim().update(Benefits("no"))
                     .update(Hours("no"))
                     .update(LivesInGB("no"))
                     .update(Over16("no"))

  "Claim" should {
    "honeypot" in {
      "returns false given did not answer any honeyPot question groups" in {
        val claim = Claim()

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given TimeOutsideUK answered yes and honeyPot filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "yes", text = Some("some text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given TimeOutsideUK answered no and honeyPot not filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", text = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given TimeOutsideUK answered no and honeyPot filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", text = Some("some text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given MoreAboutTheCare answered yes and honeyPot filled" in {
        val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given MoreAboutTheCare answered no and honeyPot not filled" in {
        val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given MoreAboutTheCare answered no and honeyPot filled" in {
        val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = Some(DayMonthYear()))))

        val result = claim.honeyPot

        result must beTrue
      }




      "returns false given ChildcareExpenses honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given ChildcareExpenses honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given ChildcareExpenses honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given PersonYouCareForExpenses honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given PersonYouCareForExpenses honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given PersonYouCareForExpenses honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given ChildcareExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given ExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given ExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given ExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given PensionSchemes answered no and honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given PensionSchemes answered yes and honeyPot filled (frequency not other)" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given PensionSchemes answered no and honeyPot filled" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given PensionSchemes honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))

        val result = claim.honeyPot

        result must beTrue
      }
    }
  }
}