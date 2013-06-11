package models.claim

import org.specs2.mutable.Specification

class SectionSpec extends Specification {

  "Section" should {
    "return the correct form" in {
      val section: Section = MockObjects.sectionOne
      val formOption = section.form(HoursForm().id)

      formOption must beLike {
        case Some(form: Form) => form.id mustEqual HoursForm().id
      }
    }
  }

}
