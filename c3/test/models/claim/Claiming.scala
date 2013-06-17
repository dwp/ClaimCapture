package models.claim

import java.util.UUID._
import org.specs2.specification.Scope
import scala.reflect.ClassTag
import org.specs2.mock.Mockito

trait Claiming extends Scope with Mockito {
  val claimKey = randomUUID().toString

  def mockForm[F <: Form](formID: String)(implicit classTag: ClassTag[F]) = {
    val form = mock[F]
    form.id returns formID
    form
  }
}