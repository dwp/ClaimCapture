package models.domain

import org.specs2.mutable.Specification

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
      val sectionID = Section.sectionID(form.id)
      val sectionOption = updatedClaim.section(sectionID)

      sectionOption must beLike {
        case Some(section: Section) => section.id mustEqual sectionID
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

      val sectionID = Section.sectionID(trueForm.id)
      val sectionOption = claimWithTrueForm.section(sectionID)
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
      val formOption = claim.questionGroup(LivesInGB)

      formOption must beLike {
        case Some(qg: QuestionGroup) => qg.id mustEqual LivesInGB.id
      }
    }

    "delete a question group from section" in {
      val claim = MockObjects.claim
      val c = claim.delete(LivesInGB)
      claim.questionGroup(LivesInGB) must be
      c.questionGroup(LivesInGB) must beNone
    }
  }
}