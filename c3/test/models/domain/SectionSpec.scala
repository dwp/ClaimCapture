package models.domain

import org.specs2.mutable.Specification

class SectionSpec extends Specification {
  "Section" should {
    "return the correct form" in {
      val section = Section(CarersAllowance.id, Benefits() :: Hours() :: LivesInGB() :: Over16() :: Nil)

      section.questionGroup(Hours) must beLike {
        case Some(qg: QuestionGroup) => qg.id mustEqual Hours.id
      }
    }

    "be able to show/hide" in {
      val section = Section(CarersAllowance.id, Benefits() :: Hours() :: LivesInGB() :: Over16() :: Nil)
      section.visible mustEqual true
      val hiddenSection = section.hide()
      hiddenSection.visible mustEqual false

      val visibleSection = hiddenSection.show()
      visibleSection.visible mustEqual true
    }

    "be able to delete a questionGroup" in {

      val benefitsGroup = Benefits()

      val section = Section(CarersAllowance.id, benefitsGroup :: Hours() :: LivesInGB() :: Over16() :: Nil)

      section.questionGroups.contains(benefitsGroup) mustEqual true

      val updatedSection = section.delete(benefitsGroup)

      updatedSection.questionGroups.contains(Benefits) mustEqual false
    }

    "be able to update a questionGroup" in {
      val section = Section(CarersAllowance.id, Benefits() :: Hours() :: LivesInGB() :: Over16() :: Nil)

      val updatedSection = section.update(Benefits(true))

      val questionGroupOption:Option[QuestionGroup] = updatedSection.questionGroup(Benefits())

      questionGroupOption must beSome(Benefits(answer = true))

    }

    "return the preceding question groups" in {
      val section = Section(CarersAllowance.id, Benefits() :: Hours() :: LivesInGB() :: Over16() :: Nil)

      val precedingGroups = section.precedingQuestionGroups(LivesInGB())

      precedingGroups(0) mustEqual Benefits()
      precedingGroups(1) mustEqual Hours()

    }
  }
}