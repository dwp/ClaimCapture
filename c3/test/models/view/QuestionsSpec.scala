package models.view

import org.scalatest._
import org.scalatest.matchers.MustMatchers
import models.view.trash.{Claim, ContactDetailsForm, ContactDetails, AboutYou}

class QuestionsSpec extends FunSpec with MustMatchers {
  describe("Questions structure") {
    it("must return the first unanswered question from the first incomplete section") {
      val claim = Claim()
      val section = claim.sections.find(section => !section.complete)
      section.get.name must equal("Eligibility")
      val qg = section.get.questionGroups.find(qg => !qg.answered)
      qg.get.name must equal("Benefits")
      qg.get.form
    }

    it("must allow the value of a question to be changed") {
      val sectionName = "AboutYou"
      val claim = Claim()
      val newClaim = Claim(claim.sections.map {
        section => {
          section.name match {
            case "AboutYou" => {
              new AboutYou(section.questionGroups.map {
                questionGroup => {
                  questionGroup.name match {
                    case "contactDetails" => {
                      new ContactDetails(ContactDetailsForm(postCode = Some("value")))
                    }
                    case _ => questionGroup
                  }
                }
              })
            }
            case _ => section
          }
        }
      })
      val qg = newClaim.sections.find(s => s.name.equals("AboutYou")).
        get.questionGroups.find(g => g.name.equals("contactDetails")).
        get
    }
  }
}
