package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.claim._
import models.claim.Hours
import models.claim.Benefits
import models.claim.LivesInGB
import models.claim.Over16

object CarersAllowance extends Controller with CachedClaim {
  val Section.allowanceId = Section.allowanceId

  val benefitsForm = Form(
    mapping(
      "answer" -> boolean
    )(Benefits.apply)(Benefits.unapply)
  )

  val hoursForm = Form(
    mapping(
      "answer" -> boolean
    )(Hours.apply)(Hours.unapply)
  )

  val livesInGBForm = Form(
    mapping(
      "answer" -> boolean
    )(LivesInGB.apply)(LivesInGB.unapply)
  )

  val over16Form = Form(
    mapping(
      "answer" -> boolean
    )(Over16.apply)(Over16.unapply)
  )

  def benefits = newClaim {
    implicit claim => implicit request =>
      val formModel = claim.form(Benefits.id).getOrElse(Benefits())
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
      val formModel = claim.form(Hours.id).getOrElse(Hours())
      claim -> Ok(views.html.s1_carersallowance.g2_hours(formModel, completedForms.takeWhile(_.id != Hours.id)))
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
      val formModel = claim.form(LivesInGB.id).getOrElse(LivesInGB())
      claim -> Ok(views.html.s1_carersallowance.g3_livesInGB(formModel, completedForms.takeWhile(_.id != LivesInGB.id)))
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
      val formModel = claim.form(Over16.id).getOrElse(Over16())
      claim -> Ok(views.html.s1_carersallowance.g4_over16(formModel, completedForms.takeWhile(_.id != Over16.id)))
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

  def approveSubmit = Action {
    Redirect(routes.AboutYou.yourDetails())
  }
}