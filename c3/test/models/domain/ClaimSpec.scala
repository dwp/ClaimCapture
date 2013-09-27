package models.domain

import org.specs2.mutable.Specification

class ClaimSpec extends Specification {
  val digitalForm: Claim = Claim().update(Benefits("no"))
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

    /*"checkTimeToCompleteAllSections" in {
      "returns true given input was too fast to be human" in {
        val digitalForm = Claim().update(Benefits("no"))
          .update(Hours("no"))
          .update(LivesInGB("no"))
          .update(Over16("no"))

        val result = digitalForm.checkTimeToCompleteAllSections(currentTime = 0)

        result must beTrue
      }

      "returns false given input was slow enough to be human" in {
        val digitalForm = Claim().update(Benefits("no"))
          .update(Hours("no"))
          .update(LivesInGB("no"))
          .update(Over16("no"))

        val result = digitalForm.checkTimeToCompleteAllSections(currentTime = Long.MaxValue)

        result must beFalse
      }
    }*/
  }

  /*"Claim" should {
    "honeypot" in {
      "returns false given did not answer any honeyPot question groups" in {
        val claim = Claim()

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given TimeOutsideUK answered yes and honeyPot filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "yes", date = Some(DayMonthYear()), text = Some("some text"), goBack = Some(YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given TimeOutsideUK answered no and honeyPot not filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", date = None, text = None, goBack = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given TimeOutsideUK answered no and honeyPot date filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", date = Some(DayMonthYear()))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given TimeOutsideUK answered no and honeyPot text filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", text = Some("some text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given TimeOutsideUK answered no and honeyPot goBack filled" in {
        val claim = Claim().update(TimeOutsideUK(LivingInUK(answer = "no", goBack = Some(YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given MoreAboutTheCare answered yes and honeyPot filled" in {
        val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "yes", date = Some(DayMonthYear()))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given MoreAboutTheCare answered no and honeyPot not filled" in {
        val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given MoreAboutTheCare answered no and honeyPot filled" in {
        val claim = Claim().update(MoreAboutTheCare(spent35HoursCaringBeforeClaim = YesNoWithDate(answer = "no", date = Some(DayMonthYear()))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given NormalResidenceAndCurrentLocation answered no and honeyPot filled" in {
        val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "no", text = Some("some text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given NormalResidenceAndCurrentLocation answered yes and honeyPot not filled" in {
        val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "yes", text = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given NormalResidenceAndCurrentLocation answered yes and honeyPot filled" in {
        val claim = Claim().update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = "yes", text = Some("some text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given NormalResidenceAndCurrentLocation honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given ChildcareExpenses honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given ChildcareExpenses honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(ChildcareExpenses(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given PersonYouCareForExpenses honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given PersonYouCareForExpenses honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given PersonYouCareForExpenses honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(PersonYouCareForExpenses(howOftenPayCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given ChildcareExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given ChildcareExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(ChildcareExpensesWhileAtWork(howOftenPayChildCare = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given ExpensesWhileAtWork honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None)))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given ExpensesWhileAtWork honeyPot not filled (frequency other and text entered)" in {
        val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Other, other = Some("other text"))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given ExpensesWhileAtWork honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(ExpensesWhileAtWork(howOftenPayExpenses = models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text"))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given PensionSchemes answered no and honeyPot not filled (frequency not other)" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = None))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given PensionSchemes answered yes and honeyPot filled (frequency not other)" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given PensionSchemes answered no and honeyPot filled" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "no", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = None))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given PensionSchemes honeyPot filled (frequency not other and text entered)" in {
        val claim = Claim().update(PensionSchemes(payPersonalPensionScheme = "yes", howOftenPersonal = Some(models.PensionPaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given AboutOtherMoney answered yes and honeyPot filled" in {
        val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("yes"), whoPaysYou = Some("some whoPaysYou")))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given AboutOtherMoney answered no and honeyPot not filled" in {
        val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), whoPaysYou = None))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given AboutOtherMoney answered no and honeyPot whoPaysYou filled" in {
        val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), whoPaysYou = Some("some whoPaysYou")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given AboutOtherMoney answered no and honeyPot howMuch filled" in {
        val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), howMuch = Some("some howMuch")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given AboutOtherMoney answered no and honeyPot howOften filled" in {
        val claim = Claim().update(AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo("no"), howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given StatutorySickPay answered yes and honeyPot filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "yes", howMuch = Some("some howMuch")))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given StatutorySickPay answered no and honeyPot not filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howMuch = None))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given StatutorySickPay answered no and honeyPot howMuch filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howMuch = Some("some howMuch")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given StatutorySickPay answered no and honeyPot howOften filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given StatutorySickPay answered no and honeyPot employersName filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersName = Some("some employersName")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given StatutorySickPay answered no and honeyPot employersAddress filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersAddress = Some(MultiLineAddress(Street(Some("some lineOne"))))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given StatutorySickPay answered no and honeyPot employersPostcode filled" in {
        val claim = Claim().update(StatutorySickPay(haveYouHadAnyStatutorySickPay = "no", employersPostcode = Some("some employersPostcode")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns false given OtherStatutoryPay answered yes and honeyPot filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "yes", howMuch = Some("some howMuch")))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns false given OtherStatutoryPay answered no and honeyPot not filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howMuch = None))

        val result = claim.honeyPot

        result must beFalse
      }

      "returns true given OtherStatutoryPay answered no and honeyPot howMuch filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howMuch = Some("some howMuch")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given OtherStatutoryPay answered no and honeyPot howOften filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "no", howOften = Some(models.PaymentFrequency(frequency = app.PensionPaymentFrequency.Weekly, other = Some("other text")))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given OtherStatutoryPay answered no and honeyPot employersName filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersName = Some("some employersName")))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given OtherStatutoryPay answered no and honeyPot employersAddress filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersAddress = Some(MultiLineAddress(Street(Some("some lineOne"))))))

        val result = claim.honeyPot

        result must beTrue
      }

      "returns true given OtherStatutoryPay answered no and honeyPot employersPostcode filled" in {
        val claim = Claim().update(OtherStatutoryPay(otherPay = "no", employersPostcode = Some("some employersPostcode")))

        val result = claim.honeyPot

        result must beTrue
      }
    }
  }*/
}