package models.domain

import org.specs2.mutable.Specification

class SectionSpec extends Specification {

  "Section" should {
    "return the correct form" in {
      val section: Section = MockObjects.sectionOne
      val formOption = section.questionGroup(Hours.id)

      formOption must beLike {
        case Some(form: QuestionGroup) => form.id mustEqual Hours.id
      }
    }
  }
}