package models.domain

import org.specs2.mutable.Specification

class ClaimSpec extends Specification {
  val claim = Claim().update(Benefits(NoRouting))
                     .update(Hours(NoRouting))
                     .update(LivesInGB(NoRouting))
                     .update(Over16(NoRouting))

  "Claim" should {
    "initially be empty" in {
      val newClaim = Claim()
      newClaim.sections.size mustEqual 0
    }

    "contain the sectionId with the question group after adding" in {
      val claim = Claim()
      val questionGroup = Benefits(NoRouting)
      val updatedClaim = claim.update(questionGroup)
      val sectionIdentifier = Section.sectionIdentifier(questionGroup)

      val section = updatedClaim.section(sectionIdentifier)
      section.identifier mustEqual sectionIdentifier
      section.questionGroup(Benefits) must beLike { case Some(Benefits(_, answer)) => answer must beFalse }
    }

    "contain the sectionId with the question group after updating" in {
      val claim = Claim()
      val trueQuestionGroup = Benefits(NoRouting, answer = true)
      val falseQuestionGroup = Benefits(NoRouting, answer = false)

      val claimWithFalseQuestionGroup = claim.update(falseQuestionGroup)
      val claimWithTrueQuestionGroup = claimWithFalseQuestionGroup.update(trueQuestionGroup)

      val sectionIdentifier = Section.sectionIdentifier(trueQuestionGroup)
      val section = claimWithTrueQuestionGroup.section(sectionIdentifier)

      section.questionGroup(Benefits) must beLike { case Some(Benefits(_, answer)) => answer must beTrue }
    }

    "return the correct section" in {
      val section = claim.section(CarersAllowance)
      section.identifier mustEqual CarersAllowance
    }

    "return the correct question group" in {
      claim.questionGroup(LivesInGB) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual LivesInGB }
    }

    "delete a question group from section" in {
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 4

      val updatedClaim = claim.delete(LivesInGB)
      updatedClaim.questionGroup(LivesInGB) must beNone
      updatedClaim.completedQuestionGroups(CarersAllowance).size mustEqual 3
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 4
    }

    "be able hide a section" in {
      val updatedClaim = new Claim().hideSection(YourPartner)
      updatedClaim.isSectionVisible(YourPartner) must beFalse
    }

    "be able show a section" in {
      val updatedClaim = new Claim().showSection(YourPartner)
      updatedClaim.isSectionVisible(YourPartner) must beTrue
    }

    "be able to update a section" in {
      val section = claim.section(CarersAllowance)
      val updatedClaim = claim.update(section.hide())
      val updatedSection = updatedClaim.section(CarersAllowance)

      updatedSection.visible must beFalse
    }

    "give previous question group within same section" in new Claiming {
      val claim = Claim().update(mockQuestionGroup[AbroadForMoreThan4Weeks](AbroadForMoreThan4Weeks))
                         .update(mockQuestionGroup[NormalResidenceAndCurrentLocation](NormalResidenceAndCurrentLocation))

      val qgiCurrent: QuestionGroup.Identifier = AbroadForMoreThan4Weeks

      claim.previousQuestionGroup(qgiCurrent) must beLike {
        case Some(qg: NormalResidenceAndCurrentLocation) => Section.sectionIdentifier(qg) shouldEqual Section.sectionIdentifier(qgiCurrent)
      }
    }

    "give last question group from previous section when asking for previous question group from start of current section" in new Claiming {
      val claim = Claim().update(mockQuestionGroup[BreaksInCare](BreaksInCare))
                         .update(mockQuestionGroup[AbroadForMoreThan4Weeks](AbroadForMoreThan4Weeks))
                         .update(mockQuestionGroup[NormalResidenceAndCurrentLocation](NormalResidenceAndCurrentLocation))

      val qgiCurrent: QuestionGroup.Identifier = NormalResidenceAndCurrentLocation

      claim.previousQuestionGroup(qgiCurrent) must beNone

      pending
    }

    "give very first question group as the previous question group" in {
      pending
    }
  }
}