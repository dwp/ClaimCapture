package models.domain

import org.specs2.mutable.Specification

class SectionSpec extends Specification {
  "Section" should {
    "return the correct question group" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[BenefitsMandatory](BenefitsMandatory) ::
                                             mockQuestionGroup[HoursMandatory](HoursMandatory) ::
                                             mockQuestionGroup[LivesInGBMandatory](LivesInGBMandatory) ::
                                             mockQuestionGroup[Over16Mandatory](Over16Mandatory) :: Nil)

      section.questionGroup(HoursMandatory) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual HoursMandatory }
    }

    "be able to show/hide" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[BenefitsMandatory](BenefitsMandatory) ::
                                             mockQuestionGroup[HoursMandatory](HoursMandatory) ::
                                             mockQuestionGroup[LivesInGBMandatory](LivesInGBMandatory) ::
                                             mockQuestionGroup[Over16Mandatory](Over16Mandatory) :: Nil)

      section.visible must beTrue
      val hiddenSection = section.hide
      hiddenSection.visible must beFalse

      val visibleSection = hiddenSection.show
      visibleSection.visible must beTrue
    }

    "be able to delete a questionGroup" in new Claiming {
      val benefits = mockQuestionGroup[BenefitsMandatory](BenefitsMandatory)

      val section = Section(CarersAllowance, benefits ::
                                             mockQuestionGroup[HoursMandatory](HoursMandatory) ::
                                             mockQuestionGroup[LivesInGBMandatory](LivesInGBMandatory) ::
                                             mockQuestionGroup[Over16Mandatory](Over16Mandatory) :: Nil)

      section.questionGroups.contains(benefits) must beTrue
      val updatedSection = section.delete(benefits)
      updatedSection.questionGroups.contains(BenefitsMandatory) must beFalse
    }

    "be able to update a questionGroup" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[BenefitsMandatory](BenefitsMandatory) ::
                                             mockQuestionGroup[HoursMandatory](HoursMandatory) ::
                                             mockQuestionGroup[LivesInGBMandatory](LivesInGBMandatory) ::
                                             mockQuestionGroup[Over16Mandatory](Over16Mandatory) :: Nil)

      val updatedSection = section.update(BenefitsMandatory(NoRouting, "yes"))
      val questionGroupOption: Option[QuestionGroup] = updatedSection.questionGroup(BenefitsMandatory)

      questionGroupOption must beLike { case Some(p: BenefitsMandatory) => p.answer must beTrue }
    }

    "return the preceding question groups" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[BenefitsMandatory](BenefitsMandatory) ::
                                             mockQuestionGroup[HoursMandatory](HoursMandatory) ::
                                             mockQuestionGroup[LivesInGBMandatory](LivesInGBMandatory) ::
                                             mockQuestionGroup[Over16Mandatory](Over16Mandatory) :: Nil)

      val precedingGroups = section.precedingQuestionGroups(LivesInGBMandatory)
      precedingGroups(0).identifier mustEqual BenefitsMandatory
      precedingGroups(1).identifier mustEqual HoursMandatory
    }
  }
}