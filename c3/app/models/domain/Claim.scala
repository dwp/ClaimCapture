package models.domain

import language.postfixOps
import models.{DayMonthYear, Timestamped}

case class Claim(sections: Map[Section.Identifier, Section] = Map()) extends Timestamped {
  def section(sectionIdentifier: Section.Identifier): Section = sections.get(sectionIdentifier) match {
    case Some(s: Section) => s
    case _ => Section(sectionIdentifier, List())
  }

  def questionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = {
    val si = Section.sectionIdentifier(questionGroupIdentifier)
    section(si).questionGroup(questionGroupIdentifier)
  }

  def previousQuestionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = completedQuestionGroups(questionGroupIdentifier).lastOption

  def completedQuestionGroups(sectionIdentifier: Section.Identifier): List[QuestionGroup] = section(sectionIdentifier).questionGroups

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier): List[QuestionGroup] = {
    val si = Section.sectionIdentifier(questionGroupIdentifier)
    section(si).precedingQuestionGroups(questionGroupIdentifier)
  }

  def update(section: Section): Claim = Claim(sections.updated(section.identifier, section))

  def update(questionGroup: QuestionGroup): Claim = {
    val si = Section.sectionIdentifier(questionGroup.identifier)
    update(section(si).update(questionGroup))
  }

  def delete(questionGroupIdentifier: QuestionGroup.Identifier): Claim = {
    val sectionIdentifier = Section.sectionIdentifier(questionGroupIdentifier)
    update(section(sectionIdentifier).delete(questionGroupIdentifier))
  }

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }

  def isSectionVisible(sectionIdentifier: Section.Identifier) = section(sectionIdentifier).visible

  def hideSection(sectionIdentifier: Section.Identifier): Claim = update(section(sectionIdentifier).hide())

  def showSection(sectionIdentifier: Section.Identifier): Claim = update(section(sectionIdentifier).show())

  def showHideSection(visible: Boolean, sectionIdentifier: Section.Identifier) = {
    if (visible) showSection(sectionIdentifier) else hideSection(sectionIdentifier)
  }
}