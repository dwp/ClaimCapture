package models.domain

import scala.reflect.ClassTag
import models.{Timestamped, DayMonthYear}
import models.view.Navigation
import scala.xml.Elem
import com.dwp.carers.s2.xml.validation.XmlValidator

/**
 * Represents the data gathered from customers through the views.
 * The data are divided into sections, themselves sub-divided into question groups.
 */
abstract class DigitalForm(val sections: List[Section] = List(), val startDigitalFormTime: Long)(implicit val navigation: Navigation = Navigation()) extends Timestamped {
  def copyForm(sections: List[Section])(implicit navigation: Navigation): DigitalForm

  def xmlValidator: XmlValidator

  def xml(transactionId: String): Elem

  def cacheKey: String

  def dateOfClaim: Option[DayMonthYear]

  def section(sectionIdentifier: Section.Identifier): Section = {
    sections.find(s => s.identifier == sectionIdentifier) match {
      case Some(s: Section) => s
      case _ => Section(sectionIdentifier, List())
    }
  }

  def previousSection(sectionIdentifier: Section.Identifier): Section = {
    sections.filter(s => s.identifier.index < sectionIdentifier.index && s.visible).lastOption match {
      case Some(s: Section) => s
      case _ => section(sectionIdentifier)
    }
  }

  def previousSection(questionGroupIdentifier: QuestionGroup.Identifier): Section = previousSection(Section.sectionIdentifier(questionGroupIdentifier))

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

  def update(section: Section) = {
    val updatedSections = sections.takeWhile(_.identifier.index < section.identifier.index) :::
      List(section) :::
      sections.dropWhile(_.identifier.index <= section.identifier.index)

    copyForm(sections = updatedSections.sortWith(_.identifier.index < _.identifier.index))
  }

  def +(section: Section) = update(section)

  def update(questionGroup: QuestionGroup): DigitalForm = {
    val si = Section.sectionIdentifier(questionGroup.identifier)
    update(section(si).update(questionGroup))
  }

  def +(questionGroup: QuestionGroup): DigitalForm = update(questionGroup)

  def delete(questionGroupIdentifier: QuestionGroup.Identifier): DigitalForm = {
    val sectionIdentifier = Section.sectionIdentifier(questionGroupIdentifier)
    update(section(sectionIdentifier).delete(questionGroupIdentifier))
  }

  def -(questionGroupIdentifier: QuestionGroup.Identifier): DigitalForm = delete(questionGroupIdentifier)

  def hideSection(sectionIdentifier: Section.Identifier): DigitalForm = update(section(sectionIdentifier).hide)

  def showSection(sectionIdentifier: Section.Identifier): DigitalForm = update(section(sectionIdentifier).show)

  def showHideSection(visible: Boolean, sectionIdentifier: Section.Identifier) = {
    if (visible) showSection(sectionIdentifier) else hideSection(sectionIdentifier)
  }

  def isBot: Boolean = if (DigitalForm.checkForBot) checkTimeToCompleteAllSections()
  else false


  def checkTimeToCompleteAllSections(currentTime: Long = System.currentTimeMillis()) = {
    val sectionExpectedTimes = Map[String, Long](
      // Change of circs
      "c1" -> 10000,
      "c2" -> 10000,
      "c3" -> 10000,
      // Claim
      "s1" -> 10000,
      "s2" -> 10000,
      "s3" -> 10000,
      "s4" -> 10000,
      "s5" -> 10000,
      "s6" -> 10000,
      "s7" -> 10000,
      "s8" -> 10000,
      "s9" -> 10000,
      "s10" -> 10000,
      "s11" -> 10000
    )

    val expectedMinTimeToCompleteAllSections: Long = sections.map(s => {
      sectionExpectedTimes.get(s.identifier.id) match {
        case Some(n) => n
        case _ => 0
      }
    }).reduce(_ + _) // Aggregate all of the sectionExpectedTimes for completed sections only.

    val actualTimeToCompleteAllSections: Long = currentTime - startDigitalFormTime
    //println("actual: " + actualTimeToCompleteAllSections + ", expected: " + expectedMinTimeToCompleteAllSections)
    (actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections)
  }
}

object DigitalForm {
  val checkForBot: Boolean = play.Configuration.root().getBoolean("checkForBot", false) //Play.configuration.getBoolean("checkForBot").getOrElse(true) //Play.current.configuration.getBoolean("checkForBot").getOrElse(true) //play.Configuration.root().getBoolean("checkForBot", true)
}