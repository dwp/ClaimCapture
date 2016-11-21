package utils.helpers

import scala.language.implicitConversions
import play.api.data.{FormError, Form}
import play.api.mvc.{AnyContent, Request}
import scala.util.{Failure, Success, Try}
import play.api.Logger

object CarersForm {
  implicit def formBinding[T](form: Form[T])(implicit request: Request[_]):FormCryptBind[T] = new FormCryptBind[T](form)

  class FormCryptBind[T](form: Form[T])(implicit request: Request[_]) {
    def bindEncrypted: Form[T] = {
      bindDecrypt(
        (request.body match {
          case body: AnyContent if body.asFormUrlEncoded.isDefined => body.asFormUrlEncoded.get
          case _ => Map.empty[String, Seq[String]]
        }) ++ request.queryString, form
      )
    }

    def bindDecrypt(map: Map[String, Seq[String]], form: Form[T]): Form[T] = {
      form.bind(
        map.foldLeft(Map.empty[String, String]) {
          case (s, (key, values)) =>
            val cKey = Try(CarersCrypto.decryptAES(key)) match {
              case Success(k) =>
                Logger.trace(s"Field decryption: $key")
                k
              case Failure(_) => key
            }

            if (cKey.endsWith("[]")) s ++ values.zipWithIndex.map {
              case (v, i) => (cKey.dropRight(2) + "[" + i + "]") -> v
            }
            else s + (cKey -> values.headOption.getOrElse("").trim)
        }
      )
    }

    def replaceError(key: String, newError: FormError): Form[T] = {
      val updatedFormErrors = form.errors.flatMap { fe =>
        if (fe.key == key) {
          if (form.error(newError.key).isDefined) None
          else {
            if (newError.args.isEmpty ) Some(FormError(newError.key,newError.message,fe.args))
            else Some(newError)
          }
        } else {
          Some(fe)
        }
      }

      form.copy(errors = updatedFormErrors.foldLeft(Seq[FormError]()) { (z, fe) =>
        if (z.groupBy(_.key).contains(fe.key)) z else z :+ fe
      })
    }

    def replaceError(key: String, message: String, newError: FormError): Form[T] = {
      def matchingError(e: FormError) = e.key == key && e.message == message
      val oldError = form.errors.find(matchingError)
      if (oldError.isDefined) {
        val error = if (newError.args.isEmpty) FormError(newError.key,newError.message,oldError.get.args) else newError
        form.copy(errors = form.errors.filterNot(e => e.key == key && e.message == message)).withError(error)
      }
      else form
    }
  }
}
