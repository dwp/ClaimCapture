package models.domain

import org.specs2.mutable.Specification

class ClaimSpec extends Specification {
  "Claim" should {
    "initially be empty" in {
      val newClaim = Claim()
      newClaim.sections.size mustEqual 0
    }

    "contain the sectionId with the question group after adding" in {
      val claim = Claim()
      val questionGroup = Benefits()
      val updatedClaim = claim.update(questionGroup)
      val sectionID = Section.sectionID(questionGroup)

      val section = updatedClaim.section(sectionID)
      section.id mustEqual sectionID
      section.questionGroup(questionGroup) must beSome(Benefits(answer = false))
    }

    "contain the sectionId with the question group after updating" in {
      val claim = Claim()
      val trueQuestionGroup = Benefits(answer = true)
      val falseQuestionGroup = Benefits(answer = false)

      val claimWithFalseQuestionGroup = claim.update(falseQuestionGroup)
      val claimWithTrueQuestionGroup = claimWithFalseQuestionGroup.update(trueQuestionGroup)

      val sectionID = Section.sectionID(trueQuestionGroup)
      val section = claimWithTrueQuestionGroup.section(sectionID)

      section.questionGroup(trueQuestionGroup) must beSome(Benefits(answer = true))
    }

    "return the correct section" in {
      val claim = Claim().update(Benefits()).update(Hours()).update(LivesInGB()).update(Over16())

      val section = claim.section(CarersAllowance.id)
      section.id mustEqual CarersAllowance.id
    }

    "return the correct question group" in {
      val claim = Claim().update(Benefits()).update(Hours()).update(LivesInGB()).update(Over16())

      claim.questionGroup(LivesInGB) must beLike {
        case Some(qg: QuestionGroup) => qg.id mustEqual LivesInGB.id
      }
    }

    "delete a question group from section" in {
      val claim = Claim().update(Benefits()).update(Hours()).update(LivesInGB()).update(Over16())
      claim.completedQuestionGroups(CarersAllowance.id).size mustEqual 4

      val updatedClaim = claim.delete(LivesInGB)
      updatedClaim.questionGroup(LivesInGB) must beNone
      updatedClaim.completedQuestionGroups(CarersAllowance.id).size mustEqual 3
      claim.completedQuestionGroups(CarersAllowance.id).size mustEqual 4
    }

    "be able hide a section" in {
      val updatedClaim = new Claim().hideSection(YourPartner.id)
      updatedClaim.isSectionVisible(YourPartner.id) mustEqual false
    }

    "be able show a section" in {
      val updatedClaim = new Claim().showSection(YourPartner.id)
      updatedClaim.isSectionVisible(YourPartner.id) mustEqual true
    }

    "be able to update a section" in {
      val claim = Claim().update(Benefits()).update(Hours()).update(LivesInGB()).update(Over16())
      val section = claim.section(CarersAllowance.id)

      val updatedClaim = claim.update(section.hide())

      val updatedSection = updatedClaim.section(CarersAllowance.id)

      updatedSection.visible mustEqual false
    }
  }
}