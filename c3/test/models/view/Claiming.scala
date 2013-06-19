package models.view

import java.util.UUID._
import org.specs2.specification.Scope
import scala.reflect.ClassTag
import org.specs2.mock.Mockito
import models.domain.QuestionGroup

trait Claiming extends Scope with Mockito {
  val claimKey = randomUUID().toString

  def mockForm[F <: QuestionGroup](formID: String)(implicit classTag: ClassTag[F]) = {
    val form = mock[F]
    form.id returns formID
    form
  }
}