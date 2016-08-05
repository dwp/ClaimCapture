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

class AssistedDecisionBlankDecisionsSpec extends Specification {
  // Do not reformat this xml it breaks the tests ... !!!
  val emptyAssistedDecisionNode = <AssistedDecision><Reason>None</Reason><RecommendedDecision>None,show table</RecommendedDecision></AssistedDecision>

  section("unit")
  "Assisted section" should {
    "Check CIS decision when all empty (happy path)" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Check CIS for benefits")
      (xml \\ "RecommendedDecision").text must contain("Potential award,show table")
    }

    "Blank decision when got trips abroad greater than 52 weeks" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val tripsAbroad = NationalityAndResidency(trip52weeks = Mappings.yes)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(tripsAbroad))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Blank decision when got breaks" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val break=Break()
      val breaksInCare = BreaksInCare().update(break)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(breaksInCare))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Blank decision when got self employed" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val employment = YourIncomes(beenSelfEmployedSince1WeekBeforeClaim = Mappings.yes, beenEmployedSince6MonthsBeforeClaim = Mappings.no, yourIncome_none = Mappings.someTrue)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(employment))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Blank decision when got employed" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val employment = YourIncomes(beenSelfEmployedSince1WeekBeforeClaim = Mappings.no, beenEmployedSince6MonthsBeforeClaim = Mappings.yes, yourIncome_none = Mappings.someTrue)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(employment))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Blank decision when got Other Income" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val employment = YourIncomes(beenSelfEmployedSince1WeekBeforeClaim = Mappings.no, beenEmployedSince6MonthsBeforeClaim = Mappings.no, yourIncome_patmatadoppay = Mappings.someTrue)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(employment))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Blank decision when no bank account" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val howWePayYou=HowWePayYou(likeToBePaid = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(howWePayYou))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }

    "Blank decision when got additional information" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Some(Mappings.yes))
      val additionalInfo=AdditionalInfo(YesNoWithText(answer = Mappings.yes, text = Some("add info")))
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(additionalInfo))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision") (0) mustEqual emptyAssistedDecisionNode
    }
  }
  section("unit")
}
