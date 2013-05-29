package models.view

import org.scalatest._
import org.scalatest.matchers.MustMatchers

class QuestionsSpec extends FunSpec with MustMatchers {
  describe("Questions structure") {
    it("must return the first unanswered question from the first incomplete section") {
      val claim = new Claim
      val section = claim.sections.find(section => !section.complete)
      section.get.name must equal("Eligibility")
      val group = section.get.questionGroups.find(group => !group.answered)
      group.get.questions.head.label must equal("Has benefits")
    }
  }
}
