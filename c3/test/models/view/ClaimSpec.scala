package models.view

import org.specs2.mutable.Specification
import utils.ClaimUtils
import models.domain._
import models.domain.Claim
import scala.Some

class ClaimSpec extends Specification {

  "Claim" should {
    "initially be empty" in {
      val newClaim = Claim()
      newClaim.sections.size mustEqual 0
    }

    "contain the sectionId with the form after adding" in {
      val claim = Claim()
      val form = Benefits()
      val updatedClaim = claim.update(form)
      val sectionId = ClaimUtils.sectionId(form.id)
      val sectionOption = updatedClaim.section(sectionId)

      sectionOption must beLike {
        case Some(section: Section) => section.id mustEqual sectionId
      }

      val section = sectionOption.get
      section.questionGroup(form.id) must beSome(Benefits(answer = false))
    }

    "contain the sectionId with the form after updating" in {
      val claim = Claim()
      val trueForm = Benefits(answer = true)
      val falseForm = Benefits(answer = false)

      val claimWithFalseForm = claim.update(falseForm)
      val claimWithTrueForm = claimWithFalseForm.update(trueForm)

      val sectionId = ClaimUtils.sectionId(trueForm.id)
      val sectionOption = claimWithTrueForm.section(sectionId)
      val section = sectionOption.get

      section.questionGroup(trueForm.id) must beSome(Benefits(answer = true))
    }

    "return the correct section" in {
      val claim = MockObjects.claim
      val sectionOneOption = claim.section(CarersAllowance.id)

      sectionOneOption must beLike {
        case Some(section: Section) => section.id mustEqual CarersAllowance.id
      }
    }

    "return the correct form" in {
      val claim = MockObjects.claim
      val formId = LivesInGB.id
      val formOption = claim.form(formId)

      formOption must beLike {
        case Some(form: QuestionGroup) => form.id mustEqual formId
      }
    }
  }
}