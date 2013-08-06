package models.domain

import org.specs2.mutable.Specification

class ClaimSpec extends Specification {
  val claim = Claim().update(BenefitsMandatory("no"))
                     .update(HoursMandatory("no"))
                     .update(LivesInGBMandatory("no"))
                     .update(Over16Mandatory("no"))

  "Claim" should {
    "initially be filled with all sections" in {
      val newClaim = Claim()
      newClaim.sections.size mustEqual 11
    }

    "contain the sectionId with the question group after adding" in {
      val claim = Claim()
      val questionGroup = BenefitsMandatory(answerYesNo = "no")
      val updatedClaim = claim.update(questionGroup)
      val sectionIdentifier = Section.sectionIdentifier(questionGroup)

      val section = updatedClaim.section(sectionIdentifier)
      section.identifier mustEqual sectionIdentifier
      section.questionGroup(BenefitsMandatory) must beLike { case Some(p: BenefitsMandatory) => p.answer must beFalse }
    }

    "contain the sectionId with the question group after updating" in {
      val claim = Claim()
      val trueQuestionGroup = BenefitsMandatory(answerYesNo = "yes")
      val falseQuestionGroup = BenefitsMandatory(answerYesNo = "no")

      val claimWithFalseQuestionGroup = claim.update(falseQuestionGroup)
      val claimWithTrueQuestionGroup = claimWithFalseQuestionGroup.update(trueQuestionGroup)

      val sectionIdentifier = Section.sectionIdentifier(trueQuestionGroup)
      val section = claimWithTrueQuestionGroup.section(sectionIdentifier)

      section.questionGroup(BenefitsMandatory) must beLike { case Some(p: BenefitsMandatory) => p.answer must beTrue }
    }

    "return the correct section" in {
      val section = claim.section(CarersAllowance)
      section.identifier mustEqual CarersAllowance
    }

    "return the correct question group" in {
      claim.questionGroup(LivesInGBMandatory) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual LivesInGBMandatory }
    }

    "delete a question group from section" in {
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 4

      val updatedClaim = claim.delete(LivesInGBMandatory)
      updatedClaim.questionGroup(LivesInGBMandatory) must beNone
      updatedClaim.completedQuestionGroups(CarersAllowance).size mustEqual 3
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 4
    }

    "be able hide a section" in {
      val updatedClaim = Claim().hideSection(YourPartner)
      updatedClaim.isSectionVisible(YourPartner) must beFalse
    }

    "be able show a section" in {
      val updatedClaim = Claim().showSection(YourPartner)
      updatedClaim.isSectionVisible(YourPartner) must beTrue
    }

    "be able to update a section" in {
      val section = claim.section(CarersAllowance)
      val updatedClaim = claim.update(section.hide)
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

    "returns first section when you ask for previous section in the first section" in {
      claim.previousSection(CarersAllowance).identifier mustEqual CarersAllowance
    }
    
    "be able to go to previous visible section" in {
      claim.previousSection(AboutYou).identifier mustEqual CarersAllowance
    }
    
    "be able to go to previous visible section when section inbetween is hidden" in {
      val updatedClaim = claim.hideSection(AboutYou)
      updatedClaim.previousSection(YourPartner).identifier mustEqual CarersAllowance
    }
    
    "be able to go to next visible section" in {
      claim.nextSection(CarersAllowance).identifier mustEqual AboutYou
    }
    
    "be able to go to next visible section when section in between is hidden" in {
      val updatedClaim = claim.hideSection(AboutYou)
      updatedClaim.nextSection(CarersAllowance).identifier mustEqual YourPartner
    }

    """not contain "question group" when not actually providing which "question group" is desired.""" in {
      claim.questionGroup should beNone
    }

    """contain "question group" in first entry of "question groups".""" in {
      claim.questionGroup[BenefitsMandatory] should beSome(BenefitsMandatory(answerYesNo = "no"))
    }

    """contain "question group" in second entry of "question groups".""" in {
      claim.questionGroup[HoursMandatory] should beSome(HoursMandatory(answerYesNo = "no"))
    }

    """not contain "question group".""" in {
      val updatedClaim = claim.delete(Over16Mandatory)
      updatedClaim.questionGroup[Over16Mandatory] should beNone
    }
  }
}