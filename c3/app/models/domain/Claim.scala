package models.domain

import utils.ClaimUtils

case class Claim(sections: Map[String, Section] = Map()) {
  val created = System.currentTimeMillis()


  def section(sectionId: String): Option[Section] = {
    sections.get(sectionId)
  }

  def form(formId: String): Option[QuestionGroup] = {
    val sectionId = ClaimUtils.sectionId(formId)

    section(sectionId) match {
      case Some(s: Section) => s.questionGroup(formId)
      case _ => None
    }
  }

  def completedFormsForSection(sectionID: String) = sections.get(sectionID) match {
    case Some(s: Section) => s.questionGroups
    case _ => Nil
  }

  def update(form: QuestionGroup): Claim = {
    def update(form: QuestionGroup, forms: List[QuestionGroup]) = {
      val updated = forms map {
        f => if (f.id == form.id) form else f
      }

      if (updated.contains(form)) updated else updated :+ form
    }

    val sectionId = ClaimUtils.sectionId(form.id)

    val section = sections.get(sectionId) match {
      case None => Section(sectionId, List(form))
      case Some(s) => Section(sectionId, update(form, s.questionGroups))
    }

    Claim(sections.updated(section.id, section))
  }
}

