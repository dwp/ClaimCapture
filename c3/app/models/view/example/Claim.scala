package models.view.example

import play.api.i18n.Messages

case class Claim(sections: Seq[Section]) {

  def getNextIncompleteSection: Option[Section] = {
    sections.find(section => !section.isComplete)
  }

  def getSectionWithId(sectionId: String): Option[Section] = {
    sections.find(section => section.name.equals(sectionId))
  }

}

object Claim {
  def apply() = {
    new Claim(Seq(
      new Section("sectionOne", Seq(
        new QuestionGroup(Messages("s1_g1"), false, SingleStringInputForm()),
        new QuestionGroup("s1.questionGroupTwo", false, SingleStringInputForm()),
        new QuestionGroup("s1.questionGroupThree", false, SingleStringInputForm())
      )),
      new Section("sectionTwo", Seq(
        new QuestionGroup("s2.questionGroupOne", false, SingleStringInputForm()),
        new QuestionGroup("s2.questionGroupTwo", false, SingleStringInputForm()),
        new QuestionGroup("s2.questionGroupThree", false, SingleStringInputForm())
      ))
    ))
  }

  def findSectionForClaim(sectionId: String, claim: Claim) = {
    claim.sections.find(section => section.name.equals(sectionId))
  }

  def findQuestionGroupForSection(sectionId: String, questionGroupId: String, claim: Claim) = {
    val sectionOption = findSectionForClaim(sectionId, claim)
    sectionOption.get.questionGroups.find(questionGroup => questionGroup.label.equals(questionGroupId))
  }

  def findFormForQuestionGroup(sectionId: String, questionGroupId: String, claim: Claim) = {
    findQuestionGroupForSection(sectionId, questionGroupId, claim).get.form
  }
}

class ClaimHolder(var claim:Claim)
