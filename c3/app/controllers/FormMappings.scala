package controllers

import play.api.data.{Form, Mapping}
import play.api.data.Forms._
import models.DayMonthYear
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime

trait FormMappings {
  val form = Form("id" -> text)

  val date: Mapping[DayMonthYear] = (mapping(
    "day" -> number,
    "month" -> number,
    "year" -> number)(DayMonthYear.apply)(DayMonthYear.unapply))

  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") { dmy =>
    Try(new DateTime(dmy.year, dmy.month, dmy.day, 0, 0)) match {
      case Success(_) => Valid
      case Failure(_) => Invalid(ValidationError("error.invalid"))
    }
  }
}