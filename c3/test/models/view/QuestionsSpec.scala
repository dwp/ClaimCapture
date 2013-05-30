package models.view

import org.scalatest._
import org.scalatest.matchers.MustMatchers

class QuestionsSpec extends FunSpec with MustMatchers {
  describe("Questions structure") {
    it("must return the first unanswered question from the first incomplete section") {
      val claim = Claim()
      val section = claim.sections.find(section => !section.complete)
      section.get.name must equal("Eligibility")
      val qg = section.get.questionGroups.find(qg => !qg.answered)
      qg.get.name must equal("Benefits")
    }

    it("must allow the value of a question to be changed") {
      val sectionName = "Eligibility"
      val claim = Claim()
      val newClaim = Claim(claim.sections.map {
        section => {
          section.name match {
            case _ => section
          }
        }
      })
    }
  }
}
