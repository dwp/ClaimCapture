package models.domain

import models.view.CachedClaim
import org.specs2.mutable.Specification

class ClaimSpec extends Specification {
  val claim: Claim = Claim(CachedClaim.key).update(Benefits(Benefits.noneOfTheBenefits))
    .update(Eligibility("no","no","no"))

  "Claim" should {
    "contain the sectionId with the question group after adding" in {
      val claim = Claim(CachedClaim.key)
      val questionGroup = Benefits(benefitsAnswer = Benefits.noneOfTheBenefits)
      val updatedClaim = claim.update(questionGroup)
      val sectionIdentifier = Section.sectionIdentifier(questionGroup)

      val section = updatedClaim.section(sectionIdentifier)
      section.identifier mustEqual sectionIdentifier
      section.questionGroup(Benefits) must beLike { case Some(p: Benefits) => p.benefitsAnswer must beEqualTo("NONE") }
    }

    "contain the sectionId with the question group after updating" in {
      val claim = Claim(CachedClaim.key)
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
      claim.questionGroup(Eligibility) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual Eligibility }
    }

    "delete a question group from section" in {
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 2

      val updatedClaim = claim.delete(Eligibility)
      updatedClaim.questionGroup(Eligibility) must beNone
      updatedClaim.completedQuestionGroups(CarersAllowance).size mustEqual 1
      claim.completedQuestionGroups(CarersAllowance).size mustEqual 2
    }

    "be able hide a section" in {
      val updatedDigitalForm = Claim(CachedClaim.key).hideSection(YourPartner)
      YourPartner.visible(updatedDigitalForm) must beFalse
    }

    "be able show a section" in {
      val updatedDigitalForm = Claim(CachedClaim.key).showSection(YourPartner)
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

    """not contain "question group" when not actually providing which "question group" is desired.""" in {
      claim.questionGroup should beNone
    }

    """contain "question group" in first entry of "question groups".""" in {
      claim.questionGroup[Benefits] should beSome(Benefits(benefitsAnswer = "NONE"))
    }

    """contain "question group" in second entry of "question groups".""" in {
      claim.questionGroup[Eligibility] should beSome(Eligibility("no", "no", "no"))
    }

    """not contain "question group".""" in {
      val updatedDigitalForm = claim.delete(Eligibility)
      updatedDigitalForm.questionGroup[Eligibility] should beNone
    }

    "iterate over jobs" in {
      val job1 = Iteration("job 1").update(JobDetails("job 1"))

      val job2 = Iteration("job 2").update(JobDetails("job 2"))

      val jobs = new Jobs(job1 :: job2 :: Nil)

      val claim = Claim(CachedClaim.key).update(jobs)

      val js = claim.questionGroup[Jobs] map { jobs =>
        for (job <- jobs) yield {
          job.iterationID
        }
      }

      js should beLike { case Some(i: Iterable[String]) => i.size shouldEqual 2 }
    }

    "iterate over no jobs" in {
      val claim = Claim(CachedClaim.key)

      val js = claim.questionGroup[Jobs] map { jobs =>
        for (job <- jobs) yield {
          job.iterationID
        }
      }

      js should beNone
    }

    "give empty list" in {
      Claim(CachedClaim.key).questionGroup(models.domain.BeenEmployed).fold(List[QuestionGroup]())(qg => List(qg)) should containAllOf(List())
    }
  }
}