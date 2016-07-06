package xml

import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import models.view.CachedClaim
import models.yesNo._
import org.joda.time.DateTime
import org.specs2.mutable._
import utils.WithApplication
import xml.claim.AssistedDecision
import org.specs2.mock.Mockito

class AssistedDecisionAgeChecksSpec extends Specification with Mockito {
  // Do not reformat this xml it breaks the tests ... !!!
  val emptyAssistedDecisionNode = <AssistedDecision><Reason>None</Reason><RecommendedDecision>None,show table</RecommendedDecision></AssistedDecision>

  lazy val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
  lazy val paymentsFromAbroad = PaymentsFromAbroad(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1 = Some(YesNoWith1MandatoryFieldOnYes(answer = Mappings.no)), field2 = Some(YesNoWith1MandatoryFieldOnYes(answer = Mappings.no))))
  lazy val benefits = Benefits(benefitsAnswer = Benefits.pip)
  lazy val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
  lazy val happyClaim = Claim(CachedClaim.key).update(moreAboutTheCare).update(paymentsFromAbroad).update(benefits).update(yourCourseDetails)

  lazy val submitDate = DateTime.now()
  lazy val fifteenAndNineInMonths = 15 * 12 + 9
  lazy val claimDateDetails = ClaimDate(DateTime.now)

  section("unit")
  "Assisted section" should {
    "No AD if customer EQUALS 15 and 9 year old today" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusMonths(fifteenAndNineInMonths))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Create AD if customer LESS THAN 15 and 9 year old today i.e. born 1 day later" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusMonths(fifteenAndNineInMonths).plusDays(1))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Customer does not turn 16 in next 3 months. Send Proforma 491 to customer.")
      (xml \\ "RecommendedDecision").text must contain("Potential disallowance decision")
    }

    "NO AD if customer OLDER THAN 15 and 9 year old today i.e. i.e. born 1 day earlier" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusMonths(fifteenAndNineInMonths).minusDays(1))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Create AD if customer EQUALS 65 claim date=today" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusYears(65))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Check CIS for benefits.")
      (xml \\ "RecommendedDecision").text must contain("Potential underlying entitlement")
    }

    "NO AD if customer 65 tomorrow and claim date=today" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusYears(65).plusDays(1))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Create AD if customer 65 yesterday claim date=today" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusYears(65).minusDays(1))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Check CIS for benefits.")
      (xml \\ "RecommendedDecision").text must contain("Potential underlying entitlement")
    }

    "Create AD if customer is 65 tomorrow but OLDER when claim date is next week" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusYears(65).plusDays(1))
      val claimDateDetails = ClaimDate(DateTime.now.plusDays(7))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Check CIS for benefits.")
      (xml \\ "RecommendedDecision").text must contain("Potential underlying entitlement")
    }

    "Happy path NO AD if customer is in the middle say 30 years old" in new WithApplication {
      val yourDetails = YourDetails(dateOfBirth = submitDate.minusYears(30))
      val claim = AssistedDecision.createAssistedDecisionDetails(happyClaim.update(claimDateDetails).update(yourDetails));
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }
}

section ("unit")
}