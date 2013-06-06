package models

import org.specs2.mutable.Specification

class ClaimSpec extends Specification {

  "Claim" should {
    "initially be empty" in {
      val newClaim = Claim()
      newClaim.sections.size mustEqual 0
    }

    "contain the section with the form after adding" in {
      val claim = Claim()
      val form = BenefitsForm()
      val updatedClaim = claim.update(form)
      val sectionId = form.section
      val sectionOption = updatedClaim.section(sectionId)

      sectionOption must beLike {
        case Some(section: Section) => section.id mustEqual sectionId
      }

      val section = sectionOption.get
      section.form(form.id) must beSome(BenefitsForm(false))
    }

    "contain the section with the form after updating" in {
      val claim = Claim()
      val trueForm = BenefitsForm(true)
      val falseForm = BenefitsForm(false)

      val claimWithFalseForm = claim.update(falseForm)

      val claimWithTrueForm = claimWithFalseForm.update(trueForm)

      val sectionId = trueForm.section
      val sectionOption = claimWithTrueForm.section(sectionId)
      val section = sectionOption.get
      section.form(trueForm.id) must beSome(BenefitsForm(true))
    }

  }

}
