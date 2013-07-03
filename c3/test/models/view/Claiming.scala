package models.view

import java.util.UUID._
import org.specs2.specification.Scope
import scala.reflect.ClassTag
import org.specs2.mock.Mockito
import models.domain.QuestionGroup

trait Claiming extends Scope with Mockito {
  val claimKey = randomUUID.toString

  def mockQuestionGroup[Q <: QuestionGroup](questionGroupID: String)(implicit classTag: ClassTag[Q]) = {
    val questionGroup = mock[Q]
    questionGroup.id returns questionGroupID
    questionGroup.index returns questionGroupID.dropWhile(!_.equals('g')).drop(1).toInt
    questionGroup
  }
}