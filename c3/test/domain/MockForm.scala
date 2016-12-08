package domain

import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import scala.reflect.ClassTag
import models.domain.{QGIdentifier, QuestionGroup}

trait MockForm extends Scope with Mockito {


  def mockQuestionGroup[Q <: QuestionGroup](qi: QGIdentifier)(implicit classTag: ClassTag[Q]): Q = {
    val questionGroup = mock[Q]
    questionGroup.identifier returns qi
    questionGroup
  }

  def mockQuestionGroup(id: String): QuestionGroup = {
    val questionGroupIdentifier = mock[QGIdentifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }
}
