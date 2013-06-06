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

  val livesInGBForm = Form(
    mapping(
      "answer" -> boolean
    )(LivesInGBForm.apply)(LivesInGBForm.unapply)
  )

  def benefits = newClaim { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")
    Ok(views.html.carersallowance.benefits(answeredForms, benefitsForm))
  }

  def benefitsSubmit = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")

    benefitsForm.bindFromRequest.fold(
      formWithErrors => Left(BadRequest(views.html.carersallowance.benefits(answeredForms, formWithErrors))),
      inputForm => Right(claim.update(inputForm) -> Redirect(routes.CarersAllowance.benefits)))
  }

  def hours = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")
    Right(claim -> Ok(views.html.carersallowance.hours(answeredForms, hoursForm)))
  }

  def hoursSubmit = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")

    hoursForm.bindFromRequest.fold(
      formWithErrors => Left(BadRequest(views.html.carersallowance.hours(answeredForms, formWithErrors))),
      inputForm => Right(claim.update(inputForm) -> Redirect(routes.CarersAllowance.hours)))
  }

  def livesInGB = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")
    Right(claim -> Ok(views.html.carersallowance.livesInGB(answeredForms, livesInGBForm)))
  }

  def livesInGBSubmit = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")

    livesInGBForm.bindFromRequest.fold(
      formWithErrors => Left(BadRequest(views.html.carersallowance.livesInGB(answeredForms, formWithErrors))),
      inputForm => Right(claim.update(inputForm) -> Redirect(routes.CarersAllowance.livesInGB)))
  }
}