package models.domain

import org.specs2.mutable._
import utils.WithApplication

class SectionSpec extends Specification {
  section("unit")
  "Section" should {
    "return the correct question group" in new WithApplication {
      val claiming = new Claiming(){}
      val section = Section(CarersAllowance, claiming.mockQuestionGroup[Benefits](Benefits) :: claiming.mockQuestionGroup[Eligibility](Eligibility) :: Nil)

      section.questionGroup(Eligibility) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual Eligibility }
    }

    "be able to show/hide" in new WithApplication {
      val claiming = new Claiming(){}
      val section = Section(CarersAllowance, claiming.mockQuestionGroup[Benefits](Benefits) :: claiming.mockQuestionGroup[Eligibility](Eligibility) :: Nil)

      section.visible must beTrue
      val hiddenSection = section.hide
      hiddenSection.visible must beFalse

      val visibleSection = hiddenSection.show
      visibleSection.visible must beTrue
    }

    "be able to delete a questionGroup" in new WithApplication {
      val claiming = new Claiming(){}
      val benefits = claiming.mockQuestionGroup[Benefits](Benefits)

      val section = Section(CarersAllowance, benefits :: claiming.mockQuestionGroup[Eligibility](Eligibility) :: Nil)

      section.questionGroups.contains(benefits) must beTrue
      val updatedSection = section.delete(benefits)
      updatedSection.questionGroups.contains(Benefits) must beFalse
    }

    "be able to update a questionGroup" in new WithApplication {
      val claiming = new Claiming(){}
      val section = Section(CarersAllowance, claiming.mockQuestionGroup[Benefits](Benefits) :: claiming.mockQuestionGroup[Eligibility](Eligibility) :: Nil)

      val updatedSection = section.update(Benefits(Benefits.aa))
      val questionGroupOption: Option[QuestionGroup] = updatedSection.questionGroup(Benefits)

      questionGroupOption must beLike { case Some(p: Benefits) => p.benefitsAnswer must beEqualTo(Benefits.aa) }
    }

    "return the preceding question groups" in new WithApplication {
      val claiming = new Claiming(){}
      val section = Section(CarersAllowance, claiming.mockQuestionGroup[Benefits](Benefits) :: claiming.mockQuestionGroup[Eligibility](Eligibility) :: Nil)

      val precedingGroups = section.precedingQuestionGroups(Eligibility)
      precedingGroups(0).identifier mustEqual Benefits
    }
  }
  section("unit")
}
