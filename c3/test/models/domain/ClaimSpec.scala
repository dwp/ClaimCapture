package models.domain

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

class ClaimSpec extends Specification with Mockito {
  "Claim" should {
    "initially be empty" in {
      val newClaim = Claim()
      newClaim.sections.size mustEqual 0
    }

    "contain the sectionId with the question group after adding" in {
      val claim = Claim()
      val questionGroup = Benefits()
      val updatedClaim = claim.update(questionGroup)
      val sectionID = Section.sectionID(questionGroup.id)
      val sectionOption = updatedClaim.section(sectionID)

      sectionOption must beLike {
        case Some(section: Section) => section.id mustEqual sectionID
      }

      val section = sectionOption.get
      section.questionGroup(questionGroup) must beSome(Benefits(answer = false))
    }

    "contain the sectionId with the question group after updating" in {
      val claim = Claim()
      val trueQuestionGroup = Benefits(answer = true)
      val falseQuestionGroup = Benefits(answer = false)

      val claimWithFalseQuestionGroup = claim.update(falseQuestionGroup)
      val claimWithTrueQuestionGroup = claimWithFalseQuestionGroup.update(trueQuestionGroup)

      val sectionID = Section.sectionID(trueQuestionGroup.id)
      val sectionOption = claimWithTrueQuestionGroup.section(sectionID)
      val section = sectionOption.get

      section.questionGroup(trueQuestionGroup) must beSome(Benefits(answer = true))
    }

    "return the correct section" in {
      val claim = Claim().update(Benefits()).update(Hours()).update(LivesInGB()).update(Over16())

      claim.section(CarersAllowance.id) must beLike {
        case Some(section: Section) => section.id mustEqual CarersAllowance.id
      }
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
  }
}