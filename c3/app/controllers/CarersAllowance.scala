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
  val Section.allowanceId = Section.allowanceId

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
      val formModel = claim.form(BenefitsForm.id).getOrElse(BenefitsForm())
      Ok(views.html.s1_carersallowance.g1_benefits(formModel))
  }

  def benefitsSubmit = claiming {
    implicit claim => implicit request =>
      benefitsForm.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.benefits()),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.hours()))
  }

  def hours = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(Section.allowanceId)
      val formModel = claim.form(HoursForm.id).getOrElse(HoursForm())
      claim -> Ok(views.html.s1_carersallowance.g2_hours(formModel, completedForms.takeWhile(_.id != HoursForm().id)))
  }

  def hoursSubmit = claiming {
    implicit claim => implicit request =>
      hoursForm.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.hours()),
        model => claim.update(model) -> Redirect(routes.CarersAllowance.livesInGB())
      )
  }

  def livesInGB = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(Section.allowanceId)
      val formModel = claim.form(LivesInGBForm.id).getOrElse(LivesInGBForm())
      claim -> Ok(views.html.s1_carersallowance.g3_livesInGB(formModel, completedForms.takeWhile(_.id != LivesInGBForm().id)))
  }

  def livesInGBSubmit = claiming {
    implicit claim => implicit request =>
      livesInGBForm.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.livesInGB()),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.over16()))
  }

  def over16 = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(Section.allowanceId)
      val formModel = claim.form(Over16Form.id).getOrElse(Over16Form())
      claim -> Ok(views.html.s1_carersallowance.g4_over16(formModel, completedForms.takeWhile(_.id != Over16Form().id)))
  }

  def over16Submit = claiming {
    implicit claim => implicit request =>
      over16Form.bindFromRequest.fold(
        formWithErrors => Redirect(routes.CarersAllowance.over16()),
        inputForm => claim.update(inputForm) -> Redirect(routes.CarersAllowance.approve()))
  }

  def approve = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(Section.allowanceId)
      val approved = claim.completedFormsForSection(Section.allowanceId).forall(_.approved) && completedForms.length == 4

      claim -> Ok(views.html.s1_carersallowance.g5_approve(approved, completedForms))
  }
}