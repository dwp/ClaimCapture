package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models._
import models.BenefitsForm

object CarersAllowance extends Controller with CachedClaim {

  val benefitsForm = Form(
    mapping(
      "answer" -> boolean
    )(BenefitsForm.apply)(BenefitsForm.unapply)
  )

  val hoursForm = Form(
    mapping(
      "answer" -> boolean
    )(HoursForm.apply)(HoursForm.unapply)
  )

  def benefits = newClaim { claim => request =>
    Ok("")
  }

  def benefitsSubmit = claiming { implicit claim => implicit request =>
    benefitsForm.bindFromRequest.fold(
      formWithErrors => Left(BadRequest(views.html.carersAllowance("test"))),
      inputForm => Right(claim.update(inputForm.copy()) -> Ok("Carer's Allowance - Command")))
  }

  def hoursSubmit = claiming { implicit claim => implicit request =>
    hoursForm.bindFromRequest.fold(
      formWithErrors => Left(BadRequest(views.html.carersAllowance("test"))),
      inputForm => Right(claim.update(inputForm.copy()) -> Ok("Carer's Allowance - Command")))
  }
}