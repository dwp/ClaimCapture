package models.domain

import org.specs2.mutable.Specification

class ClaimSpec extends Specification {
  val claim: Claim = Claim().update(Benefits(Benefits.noneOfTheBenefits))
    .update(Hours("no"))
    .update(LivesInGB("no"))
    .update(Over16("no"))

  "Claim" should {
    "contain the sectionId with the question group after adding" in {
      val claim = Claim()
      val questionGroup = Benefits(benefitsAnswer = Benefits.noneOfTheBenefits)
      val updatedClaim = claim.update(questionGroup)
      val sectionIdentifier = Section.sectionIdentifier(questionGroup)

      val section = updatedClaim.section(sectionIdentifier)
      section.identifier mustEqual sectionIdentifier
      section.questionGroup(Benefits) must beLike { case Some(p: Benefits) => p.benefitsAnswer must beEqualTo("NONE") }
    }

    "contain the sectionId with the question group after updating" in {
      val claim = Claim()
      val trueQuestionGroup = Benefits(benefitsAnswer = Benefits.aa)
      val falseQuestionGroup = Benefits(benefitsAnswer = Benefits.noneOfTheBenefits)

      val claimWithFalseQuestionGroup = claim.update(falseQuestionGroup)
      val claimWithTrueQuestionGroup = claimWithFalseQuestionGroup.update(trueQuestionGroup)

      val sectionIdentifier = Section.sectionIdentifier(trueQuestionGroup)
      val section = claimWithTrueQuestionGroup.section(sectionIdentifier)

      section.questionGroup(Benefits) must beLike { case Some(p: Benefits) => p.benefitsAnswer must beEqualTo(Benefits.aa) }
    }

    "return the correct section" in {
      val section = claim.section(CarersAllowance)
      section.identifier mustEqual CarersAllowance
    }

    "return the correct question group" in {
      claim.questionGroup(LivesInGB) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual LivesInGB }
    }

    "delete a question group from section" in {
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 4

      val updatedClaim = claim.delete(LivesInGB)
      updatedClaim.questionGroup(LivesInGB) must beNone
      updatedClaim.completedQuestionGroups(CarersAllowance).size mustEqual 3
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 4
    }

    "be able hide a section" in {
      val updatedDigitalForm = Claim().hideSection(YourPartner)
      YourPartner.visible(updatedDigitalForm) must beFalse
    }

    "be able show a section" in {
      val updatedDigitalForm = Claim().showSection(YourPartner)
      YourPartner.visible(updatedDigitalForm) must beTrue
    }

    "be able to update a section" in {
      val section = claim.section(CarersAllowance)
      val updatedClaim = claim.update(section.hide)
      val updatedSection = updatedClaim.section(CarersAllowance)

      updatedSection.visible must beFalse
    }

    "returns first section when you ask for previous section in the first section" in {
      claim.previousSection(CarersAllowance).identifier mustEqual CarersAllowance
    }

    "be able to go to previous visible section" in {
      claim.previousSection(AboutYou).identifier mustEqual CarersAllowance
    }

    "be able to go to previous visible section when section inbetween is hidden" in {
      val updatedDigitalForm = claim.hideSection(AboutYou)
      updatedDigitalForm.previousSection(YourPartner).identifier mustEqual CarersAllowance
    }

    /*
    TODO Can these commented out examples be updated to the refactored claim/navigation?
    "be able to go to next visible section" in {
      claim.nextSection(CarersAllowance).identifier mustEqual AboutYou
    }

    "be able to go to next visible section when section in between is hidden" in {
      val updatedClaim = claim.hideSection(AboutYou)
      updatedClaim.nextSection(CarersAllowance).identifier mustEqual YourPartner
    }*/

    """not contain "question group" when not actually providing which "question group" is desired.""" in {
      claim.questionGroup should beNone
    }

    """contain "question group" in first entry of "question groups".""" in {
      claim.questionGroup[Benefits] should beSome(Benefits(benefitsAnswer = "NONE"))
    }

    """contain "question group" in second entry of "question groups".""" in {
      claim.questionGroup[Hours] should beSome(Hours(answerYesNo = "no"))
    }

    """not contain "question group".""" in {
      val updatedDigitalForm = claim.delete(Over16)
      updatedDigitalForm.questionGroup[Over16] should beNone
    }

    "iterate over jobs" in {
      val job1 = Iteration("job 1").update(JobDetails("job 1"))

      val job2 = Iteration("job 2").update(JobDetails("job 2"))

      val jobs = new Jobs(job1 :: job2 :: Nil)

      val claim = Claim().update(jobs)

      val js = claim.questionGroup[Jobs] map { jobs =>
        for (job <- jobs) yield {
          job.iterationID
        }
      }

      js should beLike { case Some(i: Iterable[String]) => i.size shouldEqual 2 }
    }

    "iterate over no jobs" in {
      val claim = Claim()

      val js = claim.questionGroup[Jobs] map { jobs =>
        for (job <- jobs) yield {
          job.iterationID
        }
      }

      js should beNone
    }

    "give empty list" in {
      Claim().questionGroup(models.domain.BeenEmployed).fold(List[QuestionGroup]())(qg => List(qg)) should containAllOf(List())
    }
  }
}