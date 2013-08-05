package models.domain

import java.util.UUID._
import org.specs2.specification.Scope
import scala.reflect.ClassTag
import org.specs2.mock.Mockito

trait Claiming extends Scope with Mockito {
  val claimKey = randomUUID.toString

  def mockQuestionGroup[Q <: QuestionGroup](qi: QuestionGroup.Identifier)(implicit classTag: ClassTag[Q]): Q = {
    val questionGroup = mock[Q]
    questionGroup.identifier returns qi
    questionGroup
  }

  def mockQuestionGroup(id: String): QuestionGroup = {
    val questionGroupIdentifier = mock[QuestionGroup.Identifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }

  def mockJobQuestionGroup(id: String): QuestionGroup with Job.Identifier = {
    val questionGroupIdentifier = mock[QuestionGroup.Identifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup with Job.Identifier]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }
}