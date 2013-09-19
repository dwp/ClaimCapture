package models.domain

import org.specs2.mutable.Specification

class DigitalFormSpec extends Specification {
  val digitalForm: DigitalForm = Claim().update(Benefits("no"))
    .update(Hours("no"))
    .update(LivesInGB("no"))
    .update(Over16("no"))

  "DigitalForm" should {
    "contain the sectionId with the question group after adding" in {
      val digitalForm = Claim()
      val questionGroup = Benefits(answerYesNo = "no")
      val updatedClaim = digitalForm.update(questionGroup)
      val sectionIdentifier = Section.sectionIdentifier(questionGroup)

      val section = updatedClaim.section(sectionIdentifier)
      section.identifier mustEqual sectionIdentifier
      section.questionGroup(Benefits) must beLike { case Some(p: Benefits) => p.answer must beFalse }
    }

    "contain the sectionId with the question group after updating" in {
      val digitalForm = Claim()
      val trueQuestionGroup = Benefits(answerYesNo = "yes")
      val falseQuestionGroup = Benefits(answerYesNo = "no")

      val claimWithFalseQuestionGroup = digitalForm.update(falseQuestionGroup)
      val claimWithTrueQuestionGroup = claimWithFalseQuestionGroup.update(trueQuestionGroup)

      val sectionIdentifier = Section.sectionIdentifier(trueQuestionGroup)
      val section = claimWithTrueQuestionGroup.section(sectionIdentifier)

      section.questionGroup(Benefits) must beLike { case Some(p: Benefits) => p.answer must beTrue }
    }

    "return the correct section" in {
      val section = digitalForm.section(CarersAllowance)
      section.identifier mustEqual CarersAllowance
    }

    "return the correct question group" in {
      digitalForm.questionGroup(LivesInGB) must beLike { case Some(qg: QuestionGroup) => qg.identifier mustEqual LivesInGB }
    }

    "delete a question group from section" in {
      digitalForm.completedQuestionGroups(CarersAllowance).size mustEqual 4

      val updatedClaim = digitalForm.delete(LivesInGB)
      updatedClaim.questionGroup(LivesInGB) must beNone
      updatedClaim.completedQuestionGroups(CarersAllowance).size mustEqual 3
      digitalForm.completedQuestionGroups(CarersAllowance).size mustEqual 4
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
      val section = digitalForm.section(CarersAllowance)
      val updatedClaim = digitalForm.update(section.hide)
      val updatedSection = updatedClaim.section(CarersAllowance)

      updatedSection.visible must beFalse
    }

    "give previous question group within same section" in new Claiming {
      val digitalForm = Claim().update(mockQuestionGroup[AbroadForMoreThan4Weeks](AbroadForMoreThan4Weeks))
        .update(mockQuestionGroup[NormalResidenceAndCurrentLocation](NormalResidenceAndCurrentLocation))

      val qgiCurrent: QuestionGroup.Identifier = AbroadForMoreThan4Weeks

      digitalForm.previousQuestionGroup(qgiCurrent) must beLike {
        case Some(qg: NormalResidenceAndCurrentLocation) => Section.sectionIdentifier(qg) shouldEqual Section.sectionIdentifier(qgiCurrent)
      }
    }

    "returns first section when you ask for previous section in the first section" in {
      digitalForm.previousSection(CarersAllowance).identifier mustEqual CarersAllowance
    }

    "be able to go to previous visible section" in {
      digitalForm.previousSection(AboutYou).identifier mustEqual CarersAllowance
    }

    "be able to go to previous visible section when section inbetween is hidden" in {
      val updatedDigitalForm = digitalForm.hideSection(AboutYou)
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
      digitalForm.questionGroup should beNone
    }

    """contain "question group" in first entry of "question groups".""" in {
      digitalForm.questionGroup[Benefits] should beSome(Benefits(answerYesNo = "no"))
    }

    """contain "question group" in second entry of "question groups".""" in {
      digitalForm.questionGroup[Hours] should beSome(Hours(answerYesNo = "no"))
    }

    """not contain "question group".""" in {
      val updatedDigitalForm = digitalForm.delete(Over16)
      updatedDigitalForm.questionGroup[Over16] should beNone
    }

    "iterate over jobs" in {
      val job1 = Job("job 1").update(JobDetails("job 1"))
        .update(EmployerContactDetails("job 1"))

      val job2 = Job("job 2").update(JobDetails("job 2"))
        .update(EmployerContactDetails("job 2"))

      val jobs = new Jobs(job1 :: job2 :: Nil)

      val claim = Claim().update(jobs)

      val js = claim.questionGroup[Jobs] map { jobs =>
        for (job <- jobs) yield {
          job.jobID
        }
      }

      js should beLike { case Some(i: Iterable[String]) => i.size shouldEqual 2 }
    }

    "iterate over no jobs" in {
      val digitalForm = Claim()

      val js = digitalForm.questionGroup[Jobs] map { jobs =>
        for (job <- jobs) yield {
          job.jobID
        }
      }

      js should beNone
    }

    "give empty list" in {
      Claim().questionGroup(models.domain.BeenEmployed).fold(List[QuestionGroup]())(qg => List(qg)) should containAllOf(List())
    }

    "checkTimeToCompleteAllSections" in {
      "returns true given input was too fast to be human" in {
        val digitalForm = Claim(startDigitalFormTime = Long.MinValue).update(Benefits("no"))
          .update(Hours("no"))
          .update(LivesInGB("no"))
          .update(Over16("no"))

        val result = digitalForm.checkTimeToCompleteAllSections(currentTime = 0)

        result must beTrue
      }

      "returns false given input was slow enough to be human" in {
        val digitalForm = Claim(startDigitalFormTime = 0).update(Benefits("no"))
          .update(Hours("no"))
          .update(LivesInGB("no"))
          .update(Over16("no"))

        val result = digitalForm.checkTimeToCompleteAllSections(currentTime = Long.MaxValue)

        result must beFalse
      }
    }
  }
}