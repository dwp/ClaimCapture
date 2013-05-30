package models.view

import example.{ExampleClaim}
import org.scalatest._
import org.scalatest.matchers.MustMatchers

class QuestionsExampleSpec extends FunSpec with MustMatchers {
  describe("Questions structure") {
    it("must return the first unanswered question from the first incomplete section") {
      val claim = ExampleClaim()
      val section = claim.sections.find(section => !section.complete)

      section.get.name must equal("eligibility")
      val qg = section.get.questionGroups.find(qg => !qg.answered)
      qg.get.name must equal("benefits")

      val className = qg.get.form.getClass.getSimpleName
      className must equal ("BenefitsForm")

    }

    it("must allow the value of a question to be changed") {
      val claim = ExampleClaim()

      val form = ExampleClaim.findFormForQuestionGroup("eligibility", "benefits", claim)

      val benefitsForm:models.view.example.BenefitsForm = form.asInstanceOf[models.view.example.BenefitsForm]

      assert(benefitsForm.hasBenefits.isDefined == false)

      val newForm = models.view.example.BenefitsForm(Option(true))

      ExampleClaim.updateFormForQuestionGroup("eligibility", "benefits", newForm, claim)

      val updatedForm = ExampleClaim.findFormForQuestionGroup("eligibility", "benefits", claim).asInstanceOf[models.view.example.BenefitsForm]

      assert(updatedForm.hasBenefits.get == true)

    }
  }
}
