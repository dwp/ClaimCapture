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

  val over16Form = Form(
    mapping(
      "answer" -> boolean
    )(Over16Form.apply)(Over16Form.unapply)
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

  def over16 = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")
    Right(claim -> Ok(views.html.carersallowance.over16(answeredForms, over16Form)))
  }

  def over16Submit = claiming { implicit claim => implicit request =>
    val answeredForms = claim.answeredFormsForSection("s1")

    over16Form.bindFromRequest.fold(
      formWithErrors => Left(BadRequest(views.html.carersallowance.over16(answeredForms, formWithErrors))),
      inputForm => Right(claim.update(inputForm) -> Redirect(routes.CarersAllowance.over16)))
  }

  def approve = claiming { implicit claim => implicit request =>
    Right(claim, approved(claim.answeredFormsForSection("s1").forall(_.answer)))
  }

  def approved(yes: Boolean) = {
    if (yes) {
      Ok("Based on your answers you may be entitled to Carer’s Allowance.")
    } else {
      Ok("""Based on your answers you may not be entitled  to  Carer’s Allowance.
               If your circumstances change, you may be entitled to Carer’s Allowance.
               Find out more about Carer’s Allowance
               https://www.gov.uk/carers-allowance/how-to-claim""")
    }
  }
}