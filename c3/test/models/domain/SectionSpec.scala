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
  }
}