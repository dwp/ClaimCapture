package xml

import controllers.mappings.Mappings
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._
import models.DayMonthYear
import org.joda.time.DateTime
import utils.WithApplication
import models.yesNo._
import xml.claim.AssistedDecision

class AssistedDecisionSpec extends Specification {
  val emptyAssistedDecisionNode = <AssistedDecision><Reason>None</Reason><RecommendedDecision>None,show table</RecommendedDecision></AssistedDecision>

  section("unit")
  "Assisted section" should {
    "Create an assisted decision section if care less than 35 hours" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Not caring 35 hours a week.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance decision")
    }

    "Not create an assisted decision section if care more than 35 hours" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.pip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision")(0) mustEqual emptyAssistedDecisionNode
    }

    "Create an assisted decision section if date of claim > 3 months and 1 day" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.pip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3).plusDays(2)))
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(details).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain ("Claim date over 3 months into future.")
      (xml \\ "RecommendedDecision").text must contain("Potential disallowance decision")
    }

    "Not create an assisted decision section if date of claim <= 3 month and 1 day" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.pip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val now = DateTime.now()
      val details = ClaimDate(DayMonthYear(now.plusMonths(3)))
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(details).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision")(0) mustEqual emptyAssistedDecisionNode
    }

    "Create an assisted decision section if no EEA and no benefits" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.noneOfTheBenefits)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("DP on No QB. Check CIS.")
      (xml \\ "RecommendedDecision").text must contain ("Potential disallowance decision")
    }

    "Create an assisted decision section if no EEA and AFIP" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.afip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Assign to AFIP officer on CAMLite workflow.")
      (xml \\ "RecommendedDecision").text must contain ("None")
    }

    "Create an assisted decision section if EEA insurance or working 1" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.yes, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.yes)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.afip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Assign to AFIP officer on CAMLite workflow.")
      (xml \\ "RecommendedDecision").text must contain ("None")
    }

    "Create an assisted decision section if EEA insurance or working 2" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.yes, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.yes))))
      val benefits = Benefits(benefitsAnswer = Benefits.afip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Assign to AFIP officer on CAMLite workflow.")
      (xml \\ "RecommendedDecision").text must contain ("None")
    }

    "Create an assisted decision section if EEA insurance or working 3" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.yes, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.yes)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.yes))))
      val benefits = Benefits(benefitsAnswer = Benefits.afip)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Assign to AFIP officer on CAMLite workflow.")
      (xml \\ "RecommendedDecision").text must contain ("None")
    }

    "Not create an assisted decision section if no EEA insurance or working" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.yes, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.aa)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision")(0) mustEqual emptyAssistedDecisionNode
    }

    "Create an assisted decision section if no EEA and in education" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.caa)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.yes)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Send DS790/790B COMB to customer.")
      (xml \\ "RecommendedDecision").text must contain ("None")
    }

    "Create an assisted decision section if no EEA but yes to EEA guard question and in education" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.yes)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.yes))))
      val benefits = Benefits(benefitsAnswer = Benefits.caa)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.yes)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Send DS790/790B COMB to customer.")
      (xml \\ "RecommendedDecision").text must contain ("None")
    }

    "Not create an assisted decision section if no EEA and not in education" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val benefits = Benefits(benefitsAnswer = Benefits.aa)
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key).update(moreAboutTheCare).update(otherEEAStateOrSwitzerland).update(benefits).update(yourCourseDetails))
      val xml = AssistedDecision.xml(claim)
      (xml \\ "AssistedDecision")(0) mustEqual emptyAssistedDecisionNode
    }

    "Happy path" in new WithApplication {
      val moreAboutTheCare = MoreAboutTheCare(Mappings.yes)
      val nationality = NationalityAndResidency("British", None, "Yes", None, None, None, "No", None)
      val abroadForMoreThan52Weeks = AbroadForMoreThan52Weeks(anyTrips = Mappings.no)
      val breaksInCare = BreaksInCare()
      val employment = YourIncomes(beenSelfEmployedSince1WeekBeforeClaim = Mappings.no, beenEmployedSince6MonthsBeforeClaim = Mappings.no, yourIncome_none = Mappings.someTrue)
      val otherEEAStateOrSwitzerland = OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.no, field1=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no)), field2=Some(YesNoWith1MandatoryFieldOnYes(answer=Mappings.no))))
      val howWePayYou = HowWePayYou(likeToBePaid = Mappings.yes)
      val benefits = Benefits(benefitsAnswer = Benefits.aa)
      val additionalInfo = AdditionalInfo(anythingElse = YesNoWithText(Mappings.no))
      val yourCourseDetails = YourCourseDetails(beenInEducationSinceClaimDate = Mappings.no)
      val claim = AssistedDecision.createAssistedDecisionDetails(Claim(CachedClaim.key)
        .update(moreAboutTheCare)
        .update(nationality)
        .update(abroadForMoreThan52Weeks)
        .update(breaksInCare)
        .update(employment)
        .update(otherEEAStateOrSwitzerland)
        .update(howWePayYou)
        .update(benefits)
        .update(additionalInfo)
        .update(yourCourseDetails)
        )
      val xml = AssistedDecision.xml(claim)
      (xml \\ "Reason").text must contain("Check CIS for benefits")
      (xml \\ "RecommendedDecision").text must contain ("Potential award,show table")
    }
  }
  section("unit")
}