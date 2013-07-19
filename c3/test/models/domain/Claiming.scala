package models.domain

import java.util.UUID._
import org.specs2.specification.Scope
import scala.reflect.ClassTag
import org.specs2.mock.Mockito

trait Claiming extends Scope with Mockito {
  val claimKey = randomUUID.toString

  def mockQuestionGroup[Q <: QuestionGroup](qi: QuestionGroup.Identifier)(implicit classTag: ClassTag[Q]) = {
    val questionGroup = mock[Q]
    questionGroup.identifier returns qi
    questionGroup
  }
}