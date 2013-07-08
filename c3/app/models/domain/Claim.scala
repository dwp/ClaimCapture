package models.domain

import models.{DayMonthYear, Timestamped}

case class Claim(sections: Map[String, Section] = Map()) extends Timestamped {

  def section(sectionID: String): Section = {
    sections.get(sectionID) match {
      case Some(s: Section) => s
      case _ => Section(sectionID, List())
    }
  }

  def questionGroup(questionGroup: QuestionGroup): Option[QuestionGroup] = {
    val sectionID = Section.sectionID(questionGroup)

    section(sectionID).questionGroup(questionGroup)
  }

  def completedQuestionGroups(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.questionGroups
    case _ => Nil
  }

  def completedQuestionGroups(questionGroup: QuestionGroup) = sections.get(Section.sectionID(questionGroup)) match {
    case Some(s: Section) => s.questionGroups.takeWhile(_.index < questionGroup.index)
    case _ => Nil
  }

  def isSectionVisible(sectionID: String) = section(sectionID).visible

  def hideSection(sectionID: String): Claim = update(section(sectionID).hide())

  def showSection(sectionID: String): Claim = update(section(sectionID).show())

  def update(section: Section): Claim = Claim(sections.updated(section.id, section))

  def update(questionGroup: QuestionGroup): Claim = {

    val sectionID = Section.sectionID(questionGroup)

    update(section(sectionID).update(questionGroup))
  }

  def delete(questionGroup: QuestionGroup): Claim = {
    val sectionID = Section.sectionID(questionGroup)

    update(section(sectionID).delete(questionGroup))

  }

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }
}