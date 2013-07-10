package utils.helpers

import play.api.data.{FormError, Form}
import play.api.mvc.Request
import scala.util.{Failure, Success, Try}
import play.api.Logger
import scala.language.implicitConversions

object CarersForm {

  class FormCryptBind[T](form: Form[T])(implicit request: Request[_]) {
    def bindEncrypted: Form[T] = {
      bindDecrypt (
        (request.body match {
          case body: play.api.mvc.AnyContent if body.asFormUrlEncoded.isDefined => body.asFormUrlEncoded.get
          case _ => Map.empty[String,Seq[String]]
        }) ++ request.queryString, form
      )
    }

    def bindDecrypt(map: Map[String, Seq[String]], form: Form[T]): Form[T] = {
      form.bind(
        map.foldLeft(Map.empty[String, String]) {
          case (s, (key, values)) =>
            val cKey = Try(CarersCrypto.decryptAES(key)) match {
              case Success(s) =>
                Logger.trace(s"Field decryption: $key -> $s")
                s
              case Failure(_) => key
            }

            if (cKey.endsWith("[]")) s ++ values.zipWithIndex.map { case (v, i) => (cKey.dropRight(2) + "[" + i + "]") -> v }
            else s + (cKey -> values.headOption.getOrElse(""))
        }
      )
    }

    def replaceError(key: String, newError: FormError): Form[T] = {

      val updatedForm = form.error(key) match {
        case Some(s) => form.withError(newError)
        case None => form
      }

      updatedForm.copy(errors = updatedForm.errors.filter(p => p.key != key))
    }
  }

  implicit def formBinding[T](form: Form[T])(implicit request: Request[_]) = new FormCryptBind[T](form)
}