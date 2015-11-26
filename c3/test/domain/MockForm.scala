package domain

import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import java.util.UUID._
import scala.reflect.ClassTag
import models.domain.QuestionGroup

trait MockForm extends Scope with Mockito {


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
}
