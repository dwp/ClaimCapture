package controllers

import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation._
import mappings.Mappings._
import play.api.Logger
import play.api.data.format.Formats._

object CarersForms  {

  val carersText: Mapping[String] =
    text verifying restrictedStringText

  def carersText(minLength: Int = 0, maxLength: Int = Int.MaxValue): Mapping[String] =
    text(minLength, maxLength) verifying restrictedStringText

  val carersNonEmptyText: Mapping[String] =
    text verifying Constraints.nonEmpty verifying restrictedStringText

  def carersNonEmptyText(minLength: Int = 0, maxLength: Int = Int.MaxValue): Mapping[String] =
    nonEmptyText(minLength, maxLength) verifying restrictedStringText

  def carersEmailValidation: Mapping[String] = of[String] verifying emailAddress

  private val emailRegex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])*(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9]))+$""".r
  def emailAddress: Constraint[String] = Constraint[String]("constraint.email") { e =>
    if (e == null) Invalid(ValidationError("error.email"))
    else if (e.trim.isEmpty) Invalid(ValidationError("error.email"))
    else emailRegex.findFirstMatchIn(e).map(_ => Valid) match {
      case Some(Valid) => Valid
      case _ => { Logger.info(s"Email: invalid email - $e"); Invalid(ValidationError("error.email")) }
    }
  }
}
