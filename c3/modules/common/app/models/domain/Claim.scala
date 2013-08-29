package models.domain

import language.postfixOps
import models.{DayMonthYear, Timestamped}
import scala.reflect.ClassTag
import models.view.Navigation

case class Claim(sections: List[Section])(implicit val navigation: Navigation = Navigation()) extends Timestamped {
  def section(sectionIdentifier: Section.Identifier): Section = sections.find(s => s.identifier == sectionIdentifier) match {
    case Some(s: Section) => s
    case _ => Section(sectionIdentifier, List())
  }

  def previousSection(sectionIdentifier: Section.Identifier): Section = {
    sections.filter(s => s.identifier.index < sectionIdentifier.index && s.visible).lastOption match {
      case Some(s: Section) => s
      case _ => section(sectionIdentifier)
    }
  }

  def previousSection(questionGroupIdentifier: QuestionGroup.Identifier): Section = previousSection(Section.sectionIdentifier(questionGroupIdentifier))

  def nextSection(sectionIdentifier: Section.Identifier): Section = {
    sections.filter(s => s.identifier.index > sectionIdentifier.index && s.visible).headOption match {
      case Some(s: Section) => s
      case _ => section(sectionIdentifier)
    }
  }

  def nextSection(questionGroupIdentifier: QuestionGroup.Identifier): Section = nextSection(Section.sectionIdentifier(questionGroupIdentifier))

  def questionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = {
    val si = Section.sectionIdentifier(questionGroupIdentifier)
    section(si).questionGroup(questionGroupIdentifier)
  }

  def questionGroup[Q <: QuestionGroup](implicit classTag: ClassTag[Q]): Option[Q] = {
    def needQ(qg: QuestionGroup): Boolean = {
      qg.getClass == classTag.runtimeClass
    }

    sections.flatMap(_.questionGroups).find(needQ) match {
      case Some(q: Q) => Some(q)
      case _ => None
    }
  }

  def previousQuestionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = completedQuestionGroups(questionGroupIdentifier).lastOption

  def completedQuestionGroups(sectionIdentifier: Section.Identifier): List[QuestionGroup] = section(sectionIdentifier).questionGroups

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier): List[QuestionGroup] = {
    val si = Section.sectionIdentifier(questionGroupIdentifier)
    section(si).precedingQuestionGroups(questionGroupIdentifier)
  }

  def update(section: Section): Claim = Claim(sections.updated(section.identifier.index - 1, section))

  def +(section: Section): Claim = update(section)

  def update(questionGroup: QuestionGroup): Claim = {
    val si = Section.sectionIdentifier(questionGroup.identifier)
    update(section(si).update(questionGroup))
  }

  def +(questionGroup: QuestionGroup): Claim = update(questionGroup)

  def delete(questionGroupIdentifier: QuestionGroup.Identifier): Claim = {
    val sectionIdentifier = Section.sectionIdentifier(questionGroupIdentifier)
    update(section(sectionIdentifier).delete(questionGroupIdentifier))
  }

  def -(questionGroupIdentifier: QuestionGroup.Identifier): Claim = delete(questionGroupIdentifier)

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }

  def isSectionVisible(sectionIdentifier: Section.Identifier) = section(sectionIdentifier).visible

  def hideSection(sectionIdentifier: Section.Identifier): Claim = update(section(sectionIdentifier).hide)

  def showSection(sectionIdentifier: Section.Identifier): Claim = update(section(sectionIdentifier).show)

  def showHideSection(visible: Boolean, sectionIdentifier: Section.Identifier) = {
    if (visible) showSection(sectionIdentifier) else hideSection(sectionIdentifier)
  }
}

object Claim {
  def apply() = {
    val sections = List(
      Section(CarersAllowance, firstPage = "/allowance/benefits", lastPage = "/allowance/approve"),
      Section(AboutYou, firstPage ="/about-you/your-details", lastPage ="/about-you/completed"),
      Section(YourPartner, firstPage ="/your-partner/personal-details", lastPage ="/your-partner/completed"),
      Section(CareYouProvide, firstPage ="/care-you-provide/their-personal-details", lastPage = "/care-you-provide/completed"),
      Section(TimeSpentAbroad, firstPage ="/time-spent-abroad/normal-residence-and-current-location", lastPage ="/time-spent-abroad/completed"),
      Section(Education, firstPage ="/education/your-course-details", lastPage = "/education/completed"),
      Section(Employed, firstPage ="/employment/been-employed", lastPage ="/employment/completed"),
      Section(SelfEmployment, firstPage ="/self-employment/about-self-employment", lastPage ="/self-employment/completed"),
      Section(OtherMoney, firstPage ="/other-money/about-other-money", lastPage ="/other-money/completed"),
      Section(PayDetails, firstPage ="/pay-details/how-we-pay-you", lastPage ="/pay-details/completed"),
      Section(ConsentAndDeclaration, firstPage ="/consent-and-declaration/additional-info", lastPage = "/consent-and-declaration/completed"))
    new Claim(sections = sections)
  }
}