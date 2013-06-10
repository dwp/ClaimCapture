package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.claim._
import models.claim.HoursForm
import models.claim.BenefitsForm
import models.claim.LivesInGBForm
import models.claim.Over16Form

object CarersAllowance extends Controller with CachedClaim {
  val sectionID = "s1"

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

  def benefits = newClaim {
    implicit claim => implicit request =>
      Ok(views.html.s1_carersallowance.g1_benefits(benefitsForm))
  }

  def benefitsSubmit = claiming {
    implicit claim => implicit request =>
      benefitsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s1_carersallowance.g1_benefits(formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.hours()))
  }

  def hours = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)
      claim -> Ok(views.html.s1_carersallowance.g2_hours(completedForms, hoursForm))
  }

  def hoursSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)

      hoursForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s1_carersallowance.g2_hours(completedForms, formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.livesInGB()))
  }

  def livesInGB = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)
      claim -> Ok(views.html.s1_carersallowance.g3_livesInGB(completedForms, livesInGBForm))
  }

  def livesInGBSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)

      livesInGBForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s1_carersallowance.g3_livesInGB(completedForms, formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.over16()))
  }

  def over16 = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)
      claim -> Ok(views.html.s1_carersallowance.g4_over16(completedForms, over16Form))
  }

  def over16Submit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)

      over16Form.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s1_carersallowance.g4_over16(completedForms, formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.approve()))
  }

  def approve = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(sectionID)
      val approved = claim.completedFormsForSection(sectionID).forall(_.approved) && completedForms.length == 4
      claim -> Ok(views.html.s1_carersallowance.g5_approve(completedForms, approved))
  }

}