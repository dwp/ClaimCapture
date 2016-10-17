package xml.circumstances

import controllers.mappings.Mappings
import models.domain.{Claim, _}
import models.view.CachedChangeOfCircs
import models.yesNo._
import models.{DayMonthYear, PaymentFrequency}
import org.specs2.mutable._
import utils.WithApplication

class EmploymentPaySpec extends Specification {
  lazy val circsPension = CircumstancesEmploymentPensionExpenses(
    payIntoPension = YesNoWithText(Mappings.yes, Some("Employers pension scheme 10 a week")),
    payForThings = YesNoWithText(Mappings.yes, Some("Pay for overalls")),
    careCosts = YesNoWithText(Mappings.yes, Some("Pay mum for looking after kids")),
    moreAboutChanges = Some("Nothing else")
  )

  section("unit")
  "Circs Employment Payments" should {
    "Create correct xml for current employment paid-yet Yes'" in new WithApplication {
      val circsemployment = CircumstancesEmploymentChange(
        stillCaring = YesNoWithDate(Mappings.yes, None),
        hasWorkStartedYet = YesNoWithMutuallyExclusiveDates(Mappings.yes, Some(DayMonthYear(1, 1, 2016)), None),
        hasWorkFinishedYet = OptYesNoWithDate(Some(Mappings.no), None),
        typeOfWork = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("employed", None, None, None, None),
        paidMoneyYet = OptYesNoWithDate(None, None)
      )
      val circsOngoing = CircumstancesStartedEmploymentAndOngoing(
        beenPaid = Mappings.yes,
        howMuchPaid = "550",
        date = DayMonthYear(1, 2, 2016),
        howOften = PaymentFrequency("Weekly", None),
        monthlyPayDay = None,
        usuallyPaidSameAmount = Some(Mappings.yes)
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsOngoing).update(circsPension)
      val xml = EmploymentChange.xml(claim) \\ "EmploymentChange" \\ "StartedEmploymentAndOngoing"
      (xml \\ "BeenPaidYet" \\ "QuestionLabel").text mustEqual ("Have you been paid yet?")
      (xml \\ "BeenPaidYet" \\ "Answer").text mustEqual ("Yes")

      (xml \\ "HowMuchPaid" \\ "QuestionLabel").text mustEqual ("How much were you paid before tax or other deductions?")
      (xml \\ "HowMuchPaid" \\ "Answer").text mustEqual ("550")

      (xml \\ "PaymentDate" \\ "QuestionLabel").text mustEqual ("What date did you receive your last pay?")
      (xml \\ "PaymentDate" \\ "Answer").text mustEqual ("01-02-2016")

      (xml \\ "PayFrequency" \\ "QuestionLabel").text mustEqual ("How often are you paid?")
      (xml \\ "PayFrequency" \\ "Answer").text mustEqual ("Weekly")

      (xml \\ "UsuallyPaidSameAmount" \\ "QuestionLabel").text mustEqual ("Do you usually get the same amount each week?")
      (xml \\ "UsuallyPaidSameAmount" \\ "Answer").text mustEqual ("Yes")
    }

    "Create correct xml for current employment paid-yet No'" in new WithApplication {
      val circsemployment = CircumstancesEmploymentChange(
        stillCaring = YesNoWithDate(Mappings.yes, None),
        hasWorkStartedYet = YesNoWithMutuallyExclusiveDates(Mappings.yes, Some(DayMonthYear(1, 1, 2016)), None),
        hasWorkFinishedYet = OptYesNoWithDate(Some(Mappings.no), None),
        typeOfWork = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("employed", None, None, None, None),
        paidMoneyYet = OptYesNoWithDate(None, None)
      )
      val circsOngoing = CircumstancesStartedEmploymentAndOngoing(
        beenPaid = Mappings.no,
        howMuchPaid = "300",
        date = DayMonthYear(31, 12, 2030),
        howOften = PaymentFrequency("Weekly", None),
        monthlyPayDay = None,
        usuallyPaidSameAmount = Some(Mappings.yes)
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsOngoing).update(circsPension)
      val xml = EmploymentChange.xml(claim) \\ "EmploymentChange" \\ "StartedEmploymentAndOngoing"
      (xml \\ "BeenPaidYet" \\ "QuestionLabel").text mustEqual ("Have you been paid yet?")
      (xml \\ "BeenPaidYet" \\ "Answer").text mustEqual ("No")

      (xml \\ "HowMuchPaid" \\ "QuestionLabel").text mustEqual ("How much do you expect to be paid before tax or other deductions?")
      (xml \\ "HowMuchPaid" \\ "Answer").text mustEqual ("300")

      (xml \\ "PaymentDate" \\ "QuestionLabel").text mustEqual ("What date do you expect to be paid?")
      (xml \\ "PaymentDate" \\ "Answer").text mustEqual ("31-12-2030")

      (xml \\ "PayFrequency" \\ "QuestionLabel").text mustEqual ("How often do you expect to be paid?")
      (xml \\ "PayFrequency" \\ "Answer").text mustEqual ("Weekly")

      (xml \\ "UsuallyPaidSameAmount" \\ "QuestionLabel").text mustEqual ("Do you expect to get the same amount each week?")
      (xml \\ "UsuallyPaidSameAmount" \\ "Answer").text mustEqual ("Yes")
    }


    "Create correct xml for past employment where received last pay YES" in new WithApplication {
      val circsemployment = CircumstancesEmploymentChange(
        stillCaring = YesNoWithDate(Mappings.yes, None),
        hasWorkStartedYet = YesNoWithMutuallyExclusiveDates(Mappings.yes, Some(DayMonthYear(1, 1, 2016)), None),
        hasWorkFinishedYet = OptYesNoWithDate(Some(Mappings.yes), Some(DayMonthYear(1, 2, 2016))),
        typeOfWork = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("employed", None, None, None, None),
        paidMoneyYet = OptYesNoWithDate(None, None)
      )
      val circsPast = CircumstancesStartedAndFinishedEmployment(
        beenPaid = Mappings.yes,
        howMuchPaid = "600",
        dateLastPaid = DayMonthYear(1, 3, 2016),
        whatWasIncluded = Some("included bonus money"),
        howOften = PaymentFrequency("Weekly", None),
        monthlyPayDay = None,
        usuallyPaidSameAmount = Some(Mappings.yes),
        employerOwesYouMoney = Mappings.no,
        employerOwesYouMoneyInfo = None
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsPast).update(circsPension)
      val xml = EmploymentChange.xml(claim) \\ "EmploymentChange" \\ "StartedEmploymentAndFinished"
      (xml \\ "BeenPaidYet" \\ "QuestionLabel").text mustEqual ("Have you received your last pay?")
      (xml \\ "BeenPaidYet" \\ "Answer").text mustEqual ("Yes")

      (xml \\ "HowMuchPaid" \\ "QuestionLabel").text mustEqual ("How much were you paid before tax or other deductions?")
      (xml \\ "HowMuchPaid" \\ "Answer").text mustEqual ("600")

      (xml \\ "PaymentDate" \\ "QuestionLabel").text mustEqual ("What date did you receive your last pay?")
      (xml \\ "PaymentDate" \\ "Answer").text mustEqual ("01-03-2016")

      (xml \\ "WhatWasIncluded" \\ "QuestionLabel").text mustEqual ("What was included in this pay?")
      (xml \\ "WhatWasIncluded" \\ "Answer").text mustEqual ("included bonus money")
    }

    "Create correct xml for past employment where  received last pay NO" in new WithApplication {
      val circsemployment = CircumstancesEmploymentChange(
        stillCaring = YesNoWithDate(Mappings.yes, None),
        hasWorkStartedYet = YesNoWithMutuallyExclusiveDates(Mappings.yes, Some(DayMonthYear(1, 1, 2016)), None),
        hasWorkFinishedYet = OptYesNoWithDate(Some(Mappings.yes), Some(DayMonthYear(1, 2, 2016))),
        typeOfWork = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("employed", None, None, None, None),
        paidMoneyYet = OptYesNoWithDate(None, None)
      )
      val circsPast = CircumstancesStartedAndFinishedEmployment(
        beenPaid = Mappings.no,
        howMuchPaid = "600",
        dateLastPaid = DayMonthYear(1, 3, 2016),
        whatWasIncluded = Some("money owed"),
        howOften = PaymentFrequency("Weekly", None),
        monthlyPayDay = None,
        usuallyPaidSameAmount = Some(Mappings.yes),
        employerOwesYouMoney = Mappings.no,
        employerOwesYouMoneyInfo = None
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsPast).update(circsPension)
      val xml = EmploymentChange.xml(claim) \\ "EmploymentChange" \\ "StartedEmploymentAndFinished"
      (xml \\ "BeenPaidYet" \\ "QuestionLabel").text mustEqual ("Have you received your last pay?")
      (xml \\ "BeenPaidYet" \\ "Answer").text mustEqual ("No")

      (xml \\ "HowMuchPaid" \\ "QuestionLabel").text mustEqual ("How much do you expect to be paid before tax or other deductions?")
      (xml \\ "HowMuchPaid" \\ "Answer").text mustEqual ("600")

      (xml \\ "PaymentDate" \\ "QuestionLabel").text mustEqual ("What date do you expect to be paid?")
      (xml \\ "PaymentDate" \\ "Answer").text mustEqual ("01-03-2016")

      (xml \\ "WhatWasIncluded" \\ "QuestionLabel").text mustEqual ("What do you expect to be included in this pay?")
      (xml \\ "WhatWasIncluded" \\ "Answer").text mustEqual ("money owed")
    }

    "Create correct xml for future employment where know-how-much-paid YES'" in new WithApplication {
      val circsemployment = CircumstancesEmploymentChange(
        stillCaring = YesNoWithDate(Mappings.yes, None),
        hasWorkStartedYet = YesNoWithMutuallyExclusiveDates(Mappings.no, None, Some(DayMonthYear(1, 1, 2050))),
        hasWorkFinishedYet = OptYesNoWithDate(Some(Mappings.no), None),
        typeOfWork = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("employed", None, None, None, None),
        paidMoneyYet = OptYesNoWithDate(None, None)
      )
      val circsFuture = CircumstancesEmploymentNotStarted(
        beenPaid = Mappings.yes,
        howMuchPaid = Some("200"),
        whenExpectedToBePaidDate = Some(DayMonthYear(1, 6, 2020)),
        howOften = PaymentFrequency("Weekly", None),
        usuallyPaidSameAmount = Some(Mappings.no)
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsFuture).update(circsPension)
      val xml = EmploymentChange.xml(claim) \\ "EmploymentChange" \\ "NotStartedEmployment"
      (xml \\ "BeenPaidYet" \\ "QuestionLabel").text mustEqual ("Do you know how much you will be paid?")
      (xml \\ "BeenPaidYet" \\ "Answer").text mustEqual ("Yes")

      (xml \\ "HowMuchPaid" \\ "QuestionLabel").text mustEqual ("How much will you be paid before tax or other deductions?")
      (xml \\ "HowMuchPaid" \\ "Answer").text mustEqual ("200")

      (xml \\ "PaymentDate" \\ "QuestionLabel").text mustEqual ("What date do you expect to receive your first pay?")
      (xml \\ "PaymentDate" \\ "Answer").text mustEqual ("01-06-2020")

      (xml \\ "PayFrequency" \\ "QuestionLabel").text mustEqual ("How often will you be paid?")
      (xml \\ "PayFrequency" \\ "Answer").text mustEqual ("Weekly")

      (xml \\ "UsuallyPaidSameAmount" \\ "QuestionLabel").text mustEqual ("Are you expecting to be paid the same amount each week?")
      (xml \\ "UsuallyPaidSameAmount" \\ "Answer").text mustEqual ("No")
    }

    "Create correct xml for future employment where know-how-much-paid NO'" in new WithApplication {
      val circsemployment = CircumstancesEmploymentChange(
        stillCaring = YesNoWithDate(Mappings.yes, None),
        hasWorkStartedYet = YesNoWithMutuallyExclusiveDates(Mappings.no, None, Some(DayMonthYear(1, 1, 2050))),
        hasWorkFinishedYet = OptYesNoWithDate(Some(Mappings.no), None),
        typeOfWork = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("employed", None, None, None, None),
        paidMoneyYet = OptYesNoWithDate(None, None)
      )
      val circsFuture = CircumstancesEmploymentNotStarted(
        beenPaid = Mappings.no,
        howMuchPaid = None,
        whenExpectedToBePaidDate = None,
        howOften = PaymentFrequency("Weekly", None),
        usuallyPaidSameAmount = None
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsFuture).update(circsPension)
      val xml = EmploymentChange.xml(claim) \\ "EmploymentChange" \\ "NotStartedEmployment"
      (xml \\ "BeenPaidYet" \\ "QuestionLabel").text mustEqual ("Do you know how much you will be paid?")
      (xml \\ "BeenPaidYet" \\ "Answer").text mustEqual ("No")
      (xml \\ "HowMuchPaid" \\ "QuestionLabel").size mustEqual (0)
      (xml \\ "PaymentDate" \\ "QuestionLabel").size mustEqual (0)
      (xml \\ "PayFrequency" \\ "QuestionLabel").size mustEqual (0)
      (xml \\ "UsuallyPaidSameAmount" \\ "QuestionLabel").size mustEqual (0)
    }
  }
  section("unit")
}