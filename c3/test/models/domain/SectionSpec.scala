package models.domain

import org.specs2.mutable.Specification

class SectionSpec extends Specification {
  "Section" should {
    "return the correct question group" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[Benefits](Benefits) ::
                                             mockQuestionGroup[Hours](Hours) ::
                                             mockQuestionGroup[LivesInGB](LivesInGB) ::
                                             mockQuestionGroup[Over16](Over16) :: Nil)

      section.questionGroup(Hours) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual Hours }
    }

    "be able to show/hide" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[Benefits](Benefits) ::
                                             mockQuestionGroup[Hours](Hours) ::
                                             mockQuestionGroup[LivesInGB](LivesInGB) ::
                                             mockQuestionGroup[Over16](Over16) :: Nil)

      section.visible must beTrue
      val hiddenSection = section.hide
      hiddenSection.visible must beFalse

      val visibleSection = hiddenSection.show
      visibleSection.visible must beTrue
    }

    "be able to delete a questionGroup" in new Claiming {
      val benefits = mockQuestionGroup[Benefits](Benefits)

      val section = Section(CarersAllowance, benefits ::
                                             mockQuestionGroup[Hours](Hours) ::
                                             mockQuestionGroup[LivesInGB](LivesInGB) ::
                                             mockQuestionGroup[Over16](Over16) :: Nil)

      section.questionGroups.contains(benefits) must beTrue
      val updatedSection = section.delete(benefits)
      updatedSection.questionGroups.contains(Benefits) must beFalse
    }

    "be able to update a questionGroup" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[Benefits](Benefits) ::
                                             mockQuestionGroup[Hours](Hours) ::
                                             mockQuestionGroup[LivesInGB](LivesInGB) ::
                                             mockQuestionGroup[Over16](Over16) :: Nil)

      val updatedSection = section.update(Benefits("yes"))
      val questionGroupOption: Option[QuestionGroup] = updatedSection.questionGroup(Benefits)

      questionGroupOption must beLike { case Some(p: Benefits) => p.benefitsAnswer must beTrue }
    }

    "return the preceding question groups" in new Claiming {
      val section = Section(CarersAllowance, mockQuestionGroup[Benefits](Benefits) ::
                                             mockQuestionGroup[Hours](Hours) ::
                                             mockQuestionGroup[LivesInGB](LivesInGB) ::
                                             mockQuestionGroup[Over16](Over16) :: Nil)

      val precedingGroups = section.precedingQuestionGroups(LivesInGB)
      precedingGroups(0).identifier mustEqual Benefits
      precedingGroups(1).identifier mustEqual Hours
    }
  }
}