package models.view.example

case class ExampleClaim(sections : Seq[GenericSection])

object ExampleClaim {

  def apply() = {
    new ExampleClaim(Seq(
      Section.eligibility(),
      Section.aboutYou()
    ))
  }

  def findSectionForClaim(sectionId:String, claim:ExampleClaim) = {
    claim.sections.find(section => section.name.equals(sectionId))
  }

  def findQuestionGroupForSection( sectionId:String, questionGroupId:String, claim:ExampleClaim) = {
    val sectionOption = findSectionForClaim(sectionId, claim)
    sectionOption.get.questionGroups.find(questionGroup => questionGroup.name.equals(questionGroupId))
  }

  def findFormForQuestionGroup( sectionId:String, questionGroupId:String, claim:ExampleClaim  ) = {
    findQuestionGroupForSection(sectionId, questionGroupId, claim).get.form
  }

  def updateFormForQuestionGroup(sectionId:String, questionGroupId:String, form:Form, claim:ExampleClaim) = {
    val section = findSectionForClaim(sectionId, claim).get
    val nrOfQuestionGroups = section.questionGroups.length
    var i = 0
    while (i < nrOfQuestionGroups) {
      val questionGroup = section.questionGroups(i)

      if (questionGroup.name.equals(questionGroupId)) {
        section.questionGroups(i).form = form
      }

      i += 1
    }
 }
}
