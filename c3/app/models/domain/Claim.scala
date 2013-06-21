package models.domain

import models.Timestamped

case class Claim(sections: Map[String, Section] = Map()) extends Timestamped {
  def section(sectionId: String): Option[Section] = sections.get(sectionId)

  def questionGroup(questionGroupID: String): Option[QuestionGroup] = {
    val sectionId = Claim.sectionId(questionGroupID)

    section(sectionId) match {
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

    val sectionId = Claim.sectionId(questionGroup.id)

    val section = sections.get(sectionId) match {
      case None => Section(sectionId, List(questionGroup))
      case Some(s) => Section(sectionId, update(questionGroup, s.questionGroups))
    }

    Claim(sections.updated(section.id, section))
  }
}

object Claim {
  import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}

  val dateFormatGeneration: DateTimeFormatter = DateTimeFormat.forPattern("'DayMonthYear'('Some'(dd),'Some'(M),'Some'(yyyy),'None','None')")

  val dateFormatPrint: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

  val dateFormatXml : DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  def sectionId(formId: String) = {
    formId.split('.')(0)
  }
}