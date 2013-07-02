package models.domain

import models.{DayMonthYear, Timestamped}

case class Claim(sections: Map[String, Section] = Map()) extends Timestamped {
  def section(sectionId: String): Option[Section] = sections.get(sectionId)

  def questionGroup(questionGroupID: String): Option[QuestionGroup] = {
    val sectionID = Section.sectionID(questionGroupID)

    section(sectionID) match {
      case Some(s: Section) => s.questionGroup(questionGroupID)
      case _ => None
    }
  }

  def completedQuestionGroups(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.questionGroups
    case _ => Nil
  }

  def update(questionGroup: QuestionGroup): Claim = {
    def update(questionGroup: QuestionGroup, questionGroups: List[QuestionGroup]) = {
      val updated = questionGroups map {
        qg => if (qg.id == questionGroup.id) questionGroup else qg
      }

      if (updated.contains(questionGroup)) updated else updated :+ questionGroup
    }

    val sectionID = Section.sectionID(questionGroup.id)

    val section = sections.get(sectionID) match {
      case None => Section(sectionID, List(questionGroup))
      case Some(s) => Section(sectionID, update(questionGroup, s.questionGroups))
    }

    Claim(sections.updated(section.id, section))
  }

  def delete(questionGroupId: String): Claim = {
    val sectionID = Section.sectionID(questionGroupId)

    val section = sections.get(sectionID) match {
      case None => Section(sectionID, List())
      case Some(s) => Section(sectionID, s.questionGroups.filterNot(q => q.id == questionGroupId))
    }

    Claim(sections.updated(section.id, section))
  }

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate.id) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }
}