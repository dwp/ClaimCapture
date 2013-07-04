package models.domain

import models.{DayMonthYear, Timestamped}

case class Claim(sections: Map[String, Section] = Map()) extends Timestamped {
  def section(sectionId: String): Option[Section] = sections.get(sectionId)

  def questionGroup(questionGroup: QuestionGroup): Option[QuestionGroup] = {
    val sectionID = Section.sectionID(questionGroup.id)

    section(sectionID) match {
      case Some(s: Section) => s.questionGroup(questionGroup.id)
      case _ => None
    }
  }

  def completedQuestionGroups(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.questionGroups
    case _ => Nil
  }

  def completedQuestionGroups(questionGroup: QuestionGroup) = sections.get(Section.sectionID(questionGroup.id)) match {
    case Some(s: Section) => s.questionGroups.takeWhile(_.index < questionGroup.index)
    case _ => Nil
  }

  def update(questionGroup: QuestionGroup): Claim = {
    def update(questionGroup: QuestionGroup, questionGroups: List[QuestionGroup]): List[QuestionGroup] = {
      questionGroups.takeWhile(_.index < questionGroup.index) ::: List(questionGroup) ::: questionGroups.dropWhile(_.index <= questionGroup.index)
    }

    val sectionID = Section.sectionID(questionGroup.id)

    val section = sections.get(sectionID) match {
      case None => Section(sectionID, List(questionGroup))
      case Some(s) => Section(sectionID, update(questionGroup, s.questionGroups))
    }

    Claim(sections.updated(section.id, section))
  }

  def delete(questionGroup: QuestionGroup): Claim = {
    val sectionID = Section.sectionID(questionGroup.id)

    val section = sections.get(sectionID) match {
      case None => Section(sectionID, List())
      case Some(s) => Section(sectionID, s.questionGroups.filterNot(q => q.id == questionGroup.id))
    }

    Claim(sections.updated(section.id, section))
  }

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }
}