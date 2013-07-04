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
  }
}