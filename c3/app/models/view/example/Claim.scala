package models.view.example


case class Claim(sections: Seq[Section]) extends CreationTimeStamp {

  def getNextIncompleteSection: Option[Section] = {
    sections.find(section => !section.isComplete)
  }

  def getSectionWithId(sectionId: String): Option[Section] = {
    sections.find(section => section.id.equals(sectionId))
  }

}

object Claim {
  def apply() = {
    new Claim(Seq(
      new Section("s1", Seq(
        BenefitsForm(),
        SingleStringInputForm(),
        SingleStringInputForm()
      )),
      new Section("s2", Seq(
        SingleStringInputForm(),
        SingleStringInputForm(),
        SingleStringInputForm()
      ))
    ))
  }


  def findSectionForClaim(sectionId: String, claim: Claim) = {
    claim.sections.find(section => section.id.equals(sectionId))
  }

  def findQuestionGroupForSection(sectionId: String, questionGroupId: String, claim: Claim) = {
    val sectionOption = findSectionForClaim(sectionId, claim)
    sectionOption.get.forms.find(questionGroup => questionGroup.id.equals(questionGroupId))
  }

  def findFormForQuestionGroup(sectionId: String, questionGroupId: String, claim: Claim) = {
    findQuestionGroupForSection(sectionId, questionGroupId, claim).get
  }
}


