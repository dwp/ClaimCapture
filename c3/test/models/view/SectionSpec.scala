package models.view

import org.specs2.mutable.Specification
import models.domain.{Section, QuestionGroup, Hours}

class SectionSpec extends Specification {

  "Section" should {
    "return the correct form" in {
      val section: Section = MockObjects.sectionOne
      val formOption = section.questionGroup(Hours().id)

      formOption must beLike {
        case Some(form: QuestionGroup) => form.id mustEqual Hours().id
      }
    }
  }
}