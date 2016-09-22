package xml.circumstances

import controllers.mappings.Mappings
import models.{PaymentFrequency, DayMonthYear}
import models.domain.{Claim, _}
import models.view.CachedChangeOfCircs
import models.yesNo._
import org.specs2.mutable._
import utils.{WithApplication}

class EmploymentSpec extends Specification {
  lazy val circsPension = CircumstancesEmploymentPensionExpenses(
    payIntoPension = YesNoWithText(Mappings.yes, Some("Employers pension scheme 10 a week")),
    payForThings = YesNoWithText(Mappings.yes, Some("Pay for overalls")),
    careCosts = YesNoWithText(Mappings.yes, Some("Pay mum for looking after kids")),
    moreAboutChanges = Some("Nothing else")
  )

  section("unit")
  "Circs change employment Pension and Expenses" should {
    "Create correct xml for current employment with questions like 'Do you ...'" in new WithApplication {
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
      val xml = EmploymentChange.xml(claim)
      (xml \\ "EmploymentChange" \\ "PayIntoPension" \\ "QuestionLabel").text must contain("Do you pay into a pension")
      (xml \\ "EmploymentChange" \\ "PayIntoPension" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "PayIntoPensionWhatFor" \\ "QuestionLabel").text must contain("Give details of each pension you pay into")
      (xml \\ "EmploymentChange" \\ "PayIntoPensionWhatFor" \\ "Answer").text mustEqual ("Employers pension scheme 10 a week")

      (xml \\ "EmploymentChange" \\ "PaidForThingsToDoJob" \\ "QuestionLabel").text must contain("Do you pay for things")
      (xml \\ "EmploymentChange" \\ "PaidForThingsToDoJob" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "PaidForThingsWhatFor" \\ "QuestionLabel").text must contain("Give details of what you need to buy")
      (xml \\ "EmploymentChange" \\ "PaidForThingsWhatFor" \\ "Answer").text mustEqual ("Pay for overalls")

      (xml \\ "EmploymentChange" \\ "CareCostsForThisWork" \\ "QuestionLabel").text must contain("Do you have any care costs")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWork" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWorkWhatCosts" \\ "QuestionLabel").text must contain("Give details of who you pay")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWorkWhatCosts" \\ "Answer").text mustEqual ("Pay mum for looking after kids")

      (xml \\ "EmploymentChange" \\ "MoreAboutChanges" \\ "QuestionLabel").text must contain("Tell us more about your changes")
      (xml \\ "EmploymentChange" \\ "MoreAboutChanges" \\ "Answer").text mustEqual ("Nothing else")
    }

    "Create correct xml for past employment with questions like 'Did you ...'" in new WithApplication {
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
        whatWasIncluded = None,
        howOften = PaymentFrequency("Weekly", None),
        monthlyPayDay = None,
        usuallyPaidSameAmount = Some(Mappings.yes),
        employerOwesYouMoney = Mappings.no,
        employerOwesYouMoneyInfo = None
      )

      val claim = Claim(CachedChangeOfCircs.key).update(circsemployment).update(circsPast).update(circsPension)
      val xml = EmploymentChange.xml(claim)
      println("===== XML ======")
      println(xml \\ "EmploymentChange")
      (xml \\ "EmploymentChange" \\ "PayIntoPension" \\ "QuestionLabel").text must contain("Did you pay into a pension")
      (xml \\ "EmploymentChange" \\ "PayIntoPension" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "PayIntoPensionWhatFor" \\ "QuestionLabel").text must contain("Give details of each pension you paid into")
      (xml \\ "EmploymentChange" \\ "PayIntoPensionWhatFor" \\ "Answer").text mustEqual ("Employers pension scheme 10 a week")

      (xml \\ "EmploymentChange" \\ "PaidForThingsToDoJob" \\ "QuestionLabel").text must contain("Did you pay for things")
      (xml \\ "EmploymentChange" \\ "PaidForThingsToDoJob" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "PaidForThingsWhatFor" \\ "QuestionLabel").text must contain("Give details of what you needed to buy")
      (xml \\ "EmploymentChange" \\ "PaidForThingsWhatFor" \\ "Answer").text mustEqual ("Pay for overalls")

      (xml \\ "EmploymentChange" \\ "CareCostsForThisWork" \\ "QuestionLabel").text must contain("Did you have any care costs")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWork" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWorkWhatCosts" \\ "QuestionLabel").text must contain("Give details of who you paid")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWorkWhatCosts" \\ "Answer").text mustEqual ("Pay mum for looking after kids")

      (xml \\ "EmploymentChange" \\ "MoreAboutChanges" \\ "QuestionLabel").text must contain("Tell us more about your changes")
      (xml \\ "EmploymentChange" \\ "MoreAboutChanges" \\ "Answer").text mustEqual ("Nothing else")
    }

    "Create correct xml for future employment with questions like 'Will you ...'" in new WithApplication {
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
      val xml = EmploymentChange.xml(claim)
      (xml \\ "EmploymentChange" \\ "PayIntoPension" \\ "QuestionLabel").text must contain("Will you pay into a pension")
      (xml \\ "EmploymentChange" \\ "PayIntoPension" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "PayIntoPensionWhatFor" \\ "QuestionLabel").text must contain("Give details of each pension you will pay into")
      (xml \\ "EmploymentChange" \\ "PayIntoPensionWhatFor" \\ "Answer").text mustEqual ("Employers pension scheme 10 a week")

      (xml \\ "EmploymentChange" \\ "PaidForThingsToDoJob" \\ "QuestionLabel").text must contain("Will you pay for things")
      (xml \\ "EmploymentChange" \\ "PaidForThingsToDoJob" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "PaidForThingsWhatFor" \\ "QuestionLabel").text must contain("Give details of what you will need to buy")
      (xml \\ "EmploymentChange" \\ "PaidForThingsWhatFor" \\ "Answer").text mustEqual ("Pay for overalls")

      (xml \\ "EmploymentChange" \\ "CareCostsForThisWork" \\ "QuestionLabel").text must contain("Will you have any care costs")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWork" \\ "Answer").text mustEqual ("Yes")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWorkWhatCosts" \\ "QuestionLabel").text must contain("Give details of who you will pay")
      (xml \\ "EmploymentChange" \\ "CareCostsForThisWorkWhatCosts" \\ "Answer").text mustEqual ("Pay mum for looking after kids")

      (xml \\ "EmploymentChange" \\ "MoreAboutChanges" \\ "QuestionLabel").text must contain("Tell us more about your changes")
      (xml \\ "EmploymentChange" \\ "MoreAboutChanges" \\ "Answer").text mustEqual ("Nothing else")
    }
  }
  section("unit")
}