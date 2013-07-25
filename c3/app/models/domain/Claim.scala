package models.domain

import language.postfixOps
import models.{DayMonthYear, Timestamped}

case class Claim(sections: List[Section]) extends Timestamped {
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

  def previousQuestionGroup(questionGroupIdentifier: QuestionGroup.Identifier): Option[QuestionGroup] = completedQuestionGroups(questionGroupIdentifier).lastOption

  def completedQuestionGroups(sectionIdentifier: Section.Identifier): List[QuestionGroup] = section(sectionIdentifier).questionGroups

  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier): List[QuestionGroup] = {
    val si = Section.sectionIdentifier(questionGroupIdentifier)
    section(si).precedingQuestionGroups(questionGroupIdentifier)
  }

  def update(section: Section): Claim = Claim(sections.updated(section.identifier.index - 1, section))

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

object Claim {

  def apply() = {
    val sections = List(
      Section(CarersAllowance, firstPage = controllers.s1_carers_allowance.routes.G1Benefits.present(), lastPage = controllers.s1_carers_allowance.routes.CarersAllowance.approve()),
      Section(AboutYou, firstPage = controllers.s2_about_you.routes.G1YourDetails.present(), lastPage = controllers.s2_about_you.routes.AboutYou.completed()),
      Section(YourPartner, firstPage = controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present(), lastPage = controllers.s3_your_partner.routes.YourPartner.completed()),
      Section(CareYouProvide, firstPage = controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present(), lastPage = controllers.s4_care_you_provide.routes.CareYouProvide.completed()),
      Section(TimeSpentAbroad, firstPage = controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present(), lastPage = controllers.s5_time_spent_abroad.routes.TimeSpentAbroad.completed()),
      Section(Education, firstPage = controllers.s6_education.routes.G1YourCourseDetails.present(), lastPage = controllers.s6_education.routes.Education.completed()),
      Section(Employed, firstPage = controllers.s7_employment.routes.G1BeenEmployed.present(), lastPage = controllers.s7_employment.routes.Employment.completed()),
      Section(OtherMoney, firstPage = controllers.s8_other_money.routes.G1AboutOtherMoney.present(), lastPage = controllers.s8_other_money.routes.OtherMoney.completed()),
      Section(PayDetails, firstPage = controllers.s9_pay_details.routes.G1HowWePayYou.present(), lastPage = controllers.s9_pay_details.routes.PayDetails.completed()),
      Section(PayDetails, firstPage = controllers.s9_self_employment.routes.G1AboutSelfEmployment.present(), lastPage = controllers.s9_pay_details.routes.PayDetails.completed()), // TODO last page
      Section(ConsentAndDeclaration, firstPage = controllers.s10_consent_and_declaration.routes.G1AdditionalInfo.present(), lastPage = controllers.s10_consent_and_declaration.routes.ConsentAndDeclaration.completed()))

    new Claim(sections = sections)
  }
}